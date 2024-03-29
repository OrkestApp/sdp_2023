package com.github.orkest.ui.Camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

class CameraViewModel {

    //The camera's lens facing direction
    var lensFacing: CameraSelector =  CameraSelector.DEFAULT_FRONT_CAMERA
    //The captured image
    var imageCapture: ImageCapture = ImageCapture.Builder().build()

    /**
     * This code captures an image using the camera,
     * saves it to a temporary file,
     * and then converts the file to a URI to display it on the screen.
     */
    fun captureImage(onImageCaptured: (Uri) -> Unit, context: Context){
        // Create a temporary file to store the captured image
        val imageFile = File.createTempFile("image", ".jpg", context.cacheDir)
        val metadata = ImageCapture.Metadata()
        metadata.isReversedHorizontal = lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA
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
                    Log.e(TAG, "Image capture fail", exception)
                }
            })
    }


    //Saves the picture in device's image folder
    fun savePicture(capturedImageUri: Uri?, context: Context){
        capturedImageUri?.let { uri ->
            // create a new ContentValues object to store the image metadata
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            // get a ContentResolver instance to access the media store
            val contentResolver = context.contentResolver
            // insert a new image into the media store using the ContentValues object
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            // if the image was inserted successfully, copy the captured image to the new location
            imageUri?.let { targetUri ->
                contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }}
    }

    fun updateCameraPreview(cameraProvider: ProcessCameraProvider, lifecycleOwner: LifecycleOwner, preview: Preview, videoCaptureRecorder: VideoCapture<Recorder>, selectedMode: MutableState<Boolean>){
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            lensFacing,
            preview,
            if(selectedMode.value) videoCaptureRecorder else imageCapture
        )
    }

    
    @SuppressLint("MissingPermission")
    fun startRecordingVideo(
        context: Context,
        filenameFormat: String,
        videoCapture: VideoCapture<Recorder>,
        outputDirectory: File,
        executor: Executor,
        audioEnabled: Boolean,
        consumer: Consumer<VideoRecordEvent>
    ): Recording {
        val videoFile = File(
            outputDirectory,
            SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".mp4"
        )

        val outputOptions = FileOutputOptions.Builder(videoFile).build()

        //Start the recording process with the given executor and consumer
        return videoCapture.output
            .prepareRecording(context, outputOptions)
            .apply { if(audioEnabled) withAudioEnabled() }
            .start(executor, consumer)
    }


}
