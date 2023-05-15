package com.github.orkest.ui.Camera


import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.compose.ui.viewinterop.AndroidView
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.github.orkest.R
import com.github.orkest.data.Constants
import com.github.orkest.ui.MainActivity
import java.io.File
import kotlin.properties.Delegates
import androidx.camera.core.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import okhttp3.Route
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.core.util.Consumer
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume


//This class represents a camera component activity
class CameraView: ComponentActivity() {

    private val viewModel: CameraViewModel = CameraViewModel()

    private val roundedCornerValue = 20.dp
    private val paddingValue = 16.dp

    var hasCamera by Delegates.notNull<Boolean>()
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        hasCamera = (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

        setContent {
            var capturedVidedUri by remember { mutableStateOf<Uri?>(null) }
            /**if (capturedVideoUri != null){

            } else {
                VideoCaptureScreen(
                    onVideoCaptured = {uri ->
                    capturedVideoUri = uri
                })
            }**/
            VideoCaptureScreen(
                onVideoCaptured = {uri ->
                    capturedVidedUri = uri
                })

            /**
            // The state of the captured image is kept in a mutableStateOf variable.
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
            var capturedVideoUri by remember { mutableStateOf<Uri?>(null) }

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
                    },
                    onVideoCaptured = {uri ->
                        capturedVideoUri = uri
                    })

            }**/

        }
    }


    @Composable
    fun CameraPreview(
        lifecycleOwner: LifecycleOwner,
        onImageCaptured: (Uri) -> Unit,
        onVideoCaptured: (Uri) -> Unit
    ) {

        Box(modifier = Modifier
            .fillMaxSize()
            .testTag("Camera Preview Box")) {
            if(hasCamera) {
                //Selects the quality for the videos
                val qualitySelector = QualitySelector.fromOrderedList(
                    listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
                    FallbackStrategy.lowerQualityOrHigherThan(Quality.SD))

                // Get the application context and initialize the camera provider and image capture instances.
                val context = LocalContext.current

                val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
                //Instance of the camera provider
                val cameraProvider = cameraProviderFuture.get()

                // Initialize variables for the camera preview and preview view.
                var preview: Preview? = null
                lateinit var previewView : PreviewView

                val videoCapture: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }
                val recordingStarted: MutableState<Boolean> = remember { mutableStateOf(false) }


                //video instances
                val recorder = Recorder.Builder()
                    .setQualitySelector(qualitySelector)
                    .build()



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
                    onTakePictureClick = { viewModel.captureImage(onImageCaptured, context) },
                    modifier = Modifier
                        .padding(20.dp)
                        .size(100.dp)
                        .align(Alignment.BottomCenter)
                        .testTag("Take Picture Button")
                        /*.pointerInput(Unit){
                            detectTapGestures(
                                onPress = { viewModel.captureImage(onImageCaptured, context) },
                                onLongPress = {},

                            )
                        }*/
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

    @RequiresApi(Build.VERSION_CODES.P)
    suspend fun Context.createVideoCaptureUseCase(
        lifecycleOwner: LifecycleOwner,
        cameraSelector: CameraSelector,
        previewView: PreviewView
    ): VideoCapture<Recorder> {
        val preview = Preview.Builder()
            .build()
            .apply { setSurfaceProvider(previewView.surfaceProvider) }

        val qualitySelector = QualitySelector.from(
            Quality.FHD,
            FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
        )
        val recorder = Recorder.Builder()
            .setExecutor(mainExecutor)
            .setQualitySelector(qualitySelector)
            .build()
        val videoCapture = VideoCapture.withOutput(recorder)

        val cameraProvider = getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            videoCapture
        )

        return videoCapture
    }

    @RequiresApi(Build.VERSION_CODES.P)
    suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener(
                {
                    continuation.resume(future.get())
                },
                mainExecutor
            )
        }
    }



    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun VideoCaptureScreen(onVideoCaptured: (Uri) -> Unit) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        var recording: Recording? = remember { null }
        val previewView: PreviewView = remember { PreviewView(context) }
        val videoCapture: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }
        val recordingStarted: MutableState<Boolean> = remember { mutableStateOf(false) }

        val audioEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
        val cameraSelector: MutableState<CameraSelector> = remember {
            mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
        }


        LaunchedEffect(previewView) {
            videoCapture.value = context.createVideoCaptureUseCase(
                lifecycleOwner = lifecycleOwner,
                cameraSelector = cameraSelector.value,
                previewView = previewView
            )
        }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = {
                        if (!recordingStarted.value) {
                            videoCapture.value?.let { videoCapture ->
                                recordingStarted.value = true
                                val mediaDir = context.externalCacheDirs.firstOrNull()?.let {
                                    File(it, context.getString(R.string.app_name)).apply { mkdirs() }
                                }

                                recording = startRecordingVideo(
                                    context = context,
                                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                                    videoCapture = videoCapture,
                                    outputDirectory = if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir,
                                    executor = context.mainExecutor,
                                    audioEnabled = audioEnabled.value
                                ) { event ->
                                    if (event is VideoRecordEvent.Finalize) {
                                        val uri = event.outputResults.outputUri
                                        if (uri != Uri.EMPTY) {
                                            val uriEncoded = URLEncoder.encode(
                                                uri.toString(),
                                                StandardCharsets.UTF_8.toString()
                                            )
                                            onVideoCaptured(uri)
                                            /**save the video in database**/
                                        }
                                    }
                                }
                            }
                        } else {
                            recordingStarted.value = false
                            recording?.stop()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                ) {
                    Icon(
                        painter = painterResource(if (recordingStarted.value) R.drawable.powerrangerblue else R.drawable.blank_profile_pic), /**TODO TO CHANGE**/
                        contentDescription = "",
                        modifier = Modifier.size(64.dp)
                    )
                }

            }

    }


    private fun startRecordingVideo(
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

        return videoCapture.output
            .prepareRecording(context, outputOptions)
            .start(executor, consumer)
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
    fun TakePictureButton(
        onTakePictureClick: () -> Unit,
        modifier: Modifier){
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


    @Composable
    fun TakeVideoButton(
        onTakeVideoClick: () -> Unit,
        modifier: Modifier){

        Button(
            onClick = onTakeVideoClick,
            modifier = modifier,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
            border = BorderStroke(10.dp, Constants.COLOR_BACKGROUND)
        ) {
            Image(painterResource(id = R.drawable.powerrangerblue),
                contentDescription = "Take Video Button")
        }

    }


}
