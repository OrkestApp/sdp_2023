package com.github.orkest.ui

import androidx.compose.foundation.Image
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter

class Camera: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

            if (capturedImageUri != null) {
                Image(
                    painter = rememberImagePainter(data = capturedImageUri),
                    contentDescription = "Captured Image",
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                CameraPreview(lifecycleOwner = this,
                    onImageCaptured = { uri ->
                        capturedImageUri = uri
                    })
            }
        }
    }
}
@Composable
fun CameraPreview(
    lifecycleOwner: LifecycleOwner,
    onImageCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var preview: Preview? = null
    lateinit var previewView : PreviewView

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(factory = {
            val cameraProvider = cameraProviderFuture.get()
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

        Button(
            onClick = {
                val imageFile = createTempFile("image", ".jpg", context.cacheDir)
                val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            onImageCaptured(Uri.fromFile(imageFile))
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
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("Take Picture")
        }
    }
}
