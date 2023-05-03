package com.github.orkest.ui.Camera


import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import android.net.Uri
import android.os.Bundle
import androidx.camera.core.CameraInfo

import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.github.orkest.R
import com.github.orkest.data.Constants
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.github.orkest.ui.MainActivity
import kotlin.properties.Delegates

//This class represents a camera component activity
class CameraView: ComponentActivity() {

    private val viewModel: CameraViewModel = CameraViewModel()

    private val roundedCornerValue = 20.dp
    private val paddingValue = 16.dp

    var hasCamera by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasFrontCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        val hasBackCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

        hasCamera = hasFrontCamera and(hasBackCamera)

        setContent {
            // The state of the captured image is kept in a mutableStateOf variable.
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

            if (capturedImageUri != null) {
                // If an image is captured, it is displayed using an `Image` composable.
                CapturedImage(
                    capturedImageUri = capturedImageUri!!,
                    onBackClick = { capturedImageUri = null }
                )
            } else {
                // If an image is not yet captured, a `CameraPreview` composable is displayed.
                CameraPreview(
                    lifecycleOwner = this,
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

        Box(modifier = Modifier
            .fillMaxSize()
            .testTag("Camera Preview Box")) {
            if(hasCamera) {
                // Get the application context and initialize the camera provider and image capture instances.
                val context = LocalContext.current

                val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
                //Instance of the camera provider
                val cameraProvider = cameraProviderFuture.get()

                // Initialize variables for the camera preview and preview view.
                var preview: Preview? = null
                lateinit var previewView : PreviewView

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
                        viewModel.lensFacing,
                        preview,
                        viewModel.imageCapture
                    )
                    previewView
                }, modifier = Modifier.testTag("Camera Preview"))

                // Add a button to take the picture
                TakePictureButton(
                    onTakePictureClick = { viewModel.imagePreview(onImageCaptured, context) },
                    modifier = Modifier
                        .padding(20.dp)
                        .size(100.dp)
                        .align(Alignment.BottomCenter)
                        .testTag("Take Picture Button")
                )

                // Add the switch camera icon
                IconButton(
                    onClick = {
                        // Toggle the lens facing between front and back camera
                        viewModel.lensFacing =
                            if (viewModel.lensFacing == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            viewModel.lensFacing,
                            preview,
                            viewModel.imageCapture
                        )
                    },
                    modifier = Modifier
                        .padding(paddingValue)
                        .align(Alignment.TopEnd)
                        .testTag("Switch Camera Button")
                )
                {
                    Image(
                        painterResource(id = R.drawable.switch_camera_icon),
                        contentDescription = "Switch Camera"
                    )
                }
            } else {
                Text(text= "No camera on this device", modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("No Camera Text"))
            }

            val context = LocalContext.current
            //Add back button to get back to previous activity.
            BackButton({
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                       },
                Modifier
                    .padding(paddingValue)
                    .align(Alignment.TopStart)
                    .testTag("Back Button"))
        }
    }

    @Composable
    fun CapturedImage(
        capturedImageUri: Uri,
        onBackClick: () -> Unit
    ) {
        val context = LocalContext.current

        Box(modifier = Modifier.fillMaxSize()) {
            //Displays the captured Image
            Image(
                painter = rememberImagePainter(data = capturedImageUri),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxSize()
            )

            //Back button to get back to the camera preview
            BackButton(onBackClick = onBackClick, modifier = Modifier
                .padding(paddingValue)
                .align(Alignment.TopStart)
                .testTag("Back Button"))

            // Save button for the taken picture
            SaveButton(onSaveClick = {viewModel.savePicture(capturedImageUri, context)}, modifier = Modifier
                .padding(paddingValue)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .testTag("Save Button")
                )
        }
    }

    @Composable
    fun SaveButton(onSaveClick: ()-> Unit, modifier: Modifier){
        Button(
            onClick = onSaveClick,
            modifier = modifier,
            shape = RoundedCornerShape(roundedCornerValue),
            colors = ButtonDefaults.buttonColors(
                containerColor = Constants.COLOR_BACKGROUND,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Save Image", fontWeight = FontWeight.Bold)
        }
    }

    @Composable
    fun BackButton(onBackClick: () -> Unit, modifier: Modifier){
        //Add back button
        IconButton(
            onClick = onBackClick,
            modifier = modifier
        ) {
            Image(
                painterResource(id = R.drawable.back_button),
                contentDescription = "Back button"
            )
        }
    }

    @Composable
    fun TakePictureButton(onTakePictureClick: () -> Unit, modifier: Modifier){
        Button(
            onClick = onTakePictureClick,
            modifier = modifier,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
            border = BorderStroke(10.dp, Constants.COLOR_BACKGROUND)
        ) {
            Image(painterResource(id = R.drawable.take_picture_icon),
            contentDescription = "Take Picture Button")
        }
    }
}
