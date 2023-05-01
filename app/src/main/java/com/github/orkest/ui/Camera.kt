package com.github.orkest.ui

import android.content.ContentValues
import androidx.compose.foundation.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.github.orkest.R
import com.github.orkest.data.Constants
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight

//This class represents a camera component activity
class Camera: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // The state of the captured image is kept in a mutableStateOf variable.
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

            if (capturedImageUri != null) {
                // If an image is captured, it is displayed using an `Image` composable.
                CapturedImage(
                    capturedImageUri = capturedImageUri!!,
                    // Navigate back to the camera preview
                    onBackClick = { capturedImageUri = null }
                )
            } else {
                // If an image is not yet captured, a `CameraPreview` composable is displayed.
                CameraPreview(lifecycleOwner = this,
                    onImageCaptured = { uri ->
                        capturedImageUri = uri
                    })
            }
        }
    }


    @Composable
    fun CameraPreview(
        lifecycleOwner: LifecycleOwner,
        onImageCaptured: (Uri) -> Unit
    ) {
        // Get the application context and initialize the camera provider and image capture instances.
        val context = LocalContext.current
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        val imageCapture = remember { ImageCapture.Builder().build() }

        // Initialize variables for the camera preview and preview view.
        var preview: Preview? = null
        lateinit var previewView : PreviewView

        //The camera's lens facing direction
        val lensFacing = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)}

        //Instance of the camera provider
        val cameraProvider = cameraProviderFuture.get()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            //Creates camera preview to allow the user to take a picture
            AndroidView(factory = {
                //Get  instance of the camera provider to bind the camera preview to the device's camera
                previewView = PreviewView(context)
                previewView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                preview = Preview.Builder().build()
                preview?.setSurfaceProvider(previewView.surfaceProvider)
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
                previewView
            })

            // Add a button to take the picture
            Button(
                onClick = {
                    // Create a temporary file to store the captured image
                    val imageFile = createTempFile("image", ".jpg", context.cacheDir)
                    val metadata = ImageCapture.Metadata()
                    metadata.isReversedHorizontal = lensFacing.value == CameraSelector.DEFAULT_FRONT_CAMERA
                    // Build the output options for saving the captured image to the file
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).setMetadata(metadata).build()
                    // Take the picture using the ImageCapture object
                    imageCapture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                // Convert the saved file to a URI and pass it to the onImageCaptured callback to display it on screen
                                val savedUri = Uri.fromFile(imageFile)
                                onImageCaptured(savedUri)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                // handle error
                            }
                        })
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Constants.COLOR_BACKGROUND, contentColor = Color.Black)

            ) {
                Text(text = "Take Picture", fontWeight = FontWeight.Bold)
            }

            // Add the switch camera icon at the top right of the screen
            IconButton(
                onClick = {
                    // Toggle the lens facing between front and back camera
                    lensFacing.value = if (lensFacing.value == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        lensFacing.value,
                        preview,
                        imageCapture
                    )
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Image(
                    painterResource(id = R.drawable.switch_camera_icon),
                    contentDescription = "Switch Camera"
                )
            }

            //Add back button
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Image(
                    painterResource(id = R.drawable.back_button),
                    contentDescription = "Back button"
                )
            }
        }
    }

    @Composable
    fun CapturedImage(
        capturedImageUri: Uri,
        onBackClick: () -> Unit
    ) {
        val context = LocalContext.current
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberImagePainter(data = capturedImageUri),
                contentDescription = "Captured Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back button"
                )
            }

            // Save button
            Button(
                onClick = { capturedImageUri?.let { uri ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image.jpg")
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val contentResolver = context.contentResolver
                    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    imageUri?.let { targetUri ->
                        contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                            contentResolver.openInputStream(uri)?.use { inputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                    }}},
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Constants.COLOR_BACKGROUND,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Save Image", fontWeight = FontWeight.Bold)
            }
        }
    }

}
