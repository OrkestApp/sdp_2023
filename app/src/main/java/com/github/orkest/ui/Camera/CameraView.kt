package com.github.orkest.ui.Camera


import android.Manifest
import android.content.ContentValues.TAG
import androidx.compose.ui.viewinterop.AndroidView
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.github.orkest.R
import com.github.orkest.data.Constants
import com.github.orkest.ui.MainActivity
import kotlin.properties.Delegates
import androidx.camera.core.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import java.util.*
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView


//This class represents a camera component activity
class CameraView: ComponentActivity(){

    private val viewModel: CameraViewModel = CameraViewModel()

    private val roundedCornerValue = 20.dp
    private val paddingValue = 16.dp

    var hasCamera by Delegates.notNull<Boolean>()
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hasCamera = (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

        val hasRead = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        val hasWrite = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)


        var hasAllPermissions = false

        setContent {

            val permissions by remember{ mutableStateOf(cameraPermissionsGranted(this))}

            /**
            if(!permissions){
                Box{
                    Text(text= "Permissions need to be given to use the camera", modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("No Camera Text"))
                }
                askCameraPermissions() }
            else {
                MyApp()
            }**/


            // The state of the captured image is kept in a mutableStateOf variable.
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
            var capturedVideoUri by remember { mutableStateOf<Uri?>(null) }
            //If an image has been captured
            if(capturedImageUri != null){
                CapturedImage(capturedUri = capturedImageUri!!, isVideo = false) {
                    capturedImageUri = null
                }
            }
            //If a video has been captured
            if(capturedVideoUri != null){
                CapturedImage(capturedUri = capturedVideoUri!!, isVideo = true) {
                    capturedVideoUri = null
                }
            }

            if(capturedImageUri == null && capturedVideoUri == null) {
                // If an image or video is not yet captured, a `CameraPreview` composable is displayed.
                CameraPreview(
                    lifecycleOwner = this,
                    onImageCaptured = { uri ->
                        capturedImageUri = uri
                    },
                    onVideoCaptured = {uri ->
                        capturedVideoUri = uri
                    })
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.P)
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
                val qualitySelector = QualitySelector.from(
                    Quality.FHD,
                    FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
                )

                // Get the application context and initialize the camera provider and image capture instances.
                val context = LocalContext.current

                val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
                //Instance of the camera provider
                val cameraProvider = cameraProviderFuture.get()

                // Initialize variables for the camera preview and preview view.
                val preview: Preview?

                var recording: Recording? = remember { null }
                val previewView: PreviewView = remember { PreviewView(context) }
                val videoCapture: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }
                val recordingStarted: MutableState<Boolean> = remember { mutableStateOf(false) }

                val selectedMode: MutableState<Boolean> = remember { mutableStateOf(false) }
                lateinit var videoCaptureRecorder:  VideoCapture<Recorder>

                previewView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                preview = Preview.Builder().build().apply { setSurfaceProvider(previewView.surfaceProvider) }


                val recorder = Recorder.Builder()
                    .setExecutor(mainExecutor)
                    .setQualitySelector(qualitySelector)
                    .build()
                videoCaptureRecorder = VideoCapture.withOutput(recorder)

                
                if(selectedMode.value){
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        viewModel.lensFacing,
                        preview,
                        videoCaptureRecorder
                    )
                } else {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        viewModel.lensFacing,
                        preview,
                        viewModel.imageCapture
                    )
                }


                //Creates camera preview to allow the user to take a picture
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.testTag("Camera Preview")
                )




                Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                    Row { SelectCameraMode(selectedMode = selectedMode) }

                    Row {
                        if(selectedMode.value){
                            videoCapture.value = videoCaptureRecorder
                            //Add a button to take the video
                            IconButton(
                                onClick = {
                                    //Check is recording has not already started
                                    if (!recordingStarted.value) {
                                        videoCapture.value?.let { videoCapture ->
                                            recordingStarted.value = true
                                            val tempDir = context.cacheDir

                                            recording = startRecordingVideo(
                                                context = context,
                                                filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                                                videoCapture = videoCapture,
                                                outputDirectory = tempDir,
                                                executor = context.mainExecutor,
                                                audioEnabled = true
                                            ) { event ->
                                                if (event is VideoRecordEvent.Finalize) {
                                                    val uri = event.outputResults.outputUri
                                                    if (uri != Uri.EMPTY) {
                                                        /**save the video in database**/
                                                        onVideoCaptured(uri)
                                                    } else {
                                                        Log.e(TAG, "Could not record the video")
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
                                    .padding(20.dp)
                                    .size(100.dp)

                            ) {
                                Icon(
                                    painter = painterResource(if (recordingStarted.value) R.drawable.stop_recording else R.drawable.start_recording),
                                    contentDescription = "Recording video button"
                                )
                            }
                        } else {
                            // Add a button to take the picture
                            TakePictureButton(
                                onTakePictureClick = {
                                    viewModel.captureImage(
                                        onImageCaptured,
                                        context
                                    )
                                },
                                modifier = Modifier
                                    .padding(20.dp)
                                    .size(100.dp)
                                    .testTag("Take Picture Button")
                            )
                        }


                    }
                }

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
                            if(selectedMode.value) videoCaptureRecorder else viewModel.imageCapture
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
    fun SelectCameraMode(selectedMode: MutableState<Boolean>){
            Button(
                onClick = {
                    if (selectedMode.value) {
                        selectedMode.value = false
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        1.dp,
                        if (!selectedMode.value) Color.White else Color.Transparent
                    )
            ) {
                Text(text = "Photo")
            }
            Button(
                onClick = {
                    if (!selectedMode.value) {
                        selectedMode.value = true
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        1.dp,
                        if (selectedMode.value) Color.White else Color.Transparent
                    )
            ) {
                Text(text = "Video")
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
                        //Check is recording has not already started
                        if (!recordingStarted.value) {
                            videoCapture.value?.let { videoCapture ->
                                recordingStarted.value = true
                                val tempDir = context.cacheDir

                                recording = startRecordingVideo(
                                    context = context,
                                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                                    videoCapture = videoCapture,
                                    outputDirectory = tempDir,
                                    executor = context.mainExecutor,
                                    audioEnabled = audioEnabled.value
                                ) { event ->
                                    if (event is VideoRecordEvent.Finalize) {
                                        val uri = event.outputResults.outputUri
                                        if (uri != Uri.EMPTY) {
                                            /**save the video uri in database**/
                                            onVideoCaptured(uri)
                                        } else {
                                            Log.e(TAG, "Could not record the video")
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
                    Image(
                        painter = painterResource(if (recordingStarted.value) R.drawable.powerrangerblue else R.drawable.blank_profile_pic), /**TODO TO CHANGE**/
                        contentDescription = "",
                        modifier = Modifier.size(64.dp)
                    )
                }

            }

    }
























    @Composable
    fun CapturedImage(
        capturedUri: Uri,
        isVideo: Boolean,
        onBackClick: () -> Unit
    ) {
        val context = LocalContext.current
        lateinit var exoPlayer: ExoPlayer

        Box(modifier = Modifier.fillMaxSize()) {
            if (isVideo){
                //Displays the taken video
                exoPlayer = remember(context) {
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(MediaItem.fromUri(capturedUri))
                        prepare()
                    }
                }
                AndroidView(
                    factory = { context ->
                        StyledPlayerView(context).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

            } else {
                //Displays the captured Image
                Image(
                    painter = rememberImagePainter(data = capturedUri),
                    contentDescription = "Captured Image",
                    modifier = Modifier.fillMaxSize()
                )
            }

            //Back button to get back to the camera preview
            BackButton(onBackClick =
            {
                if(isVideo) { exoPlayer.release() }
                onBackClick()
            },
                modifier = Modifier
                    .padding(paddingValue)
                    .align(Alignment.TopStart)
                    .testTag("Back Button"))

            // Save button for the taken picture
            SaveButton(onSaveClick = {
                if (isVideo) {

                } else {
                    viewModel.savePicture(capturedUri, context)
                }
            }, modifier = Modifier
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


    /**
     * Returns true if the app has the necessary permissions for recording.
     */
    private fun cameraPermissionsGranted(context: Context): Boolean {
        val permissions = arrayOf(
            //Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Requests the necessary permissions for recording.
     * This method shows the permission request dialogs.
     */
    private fun askCameraPermissions() {
        val permissions = arrayOf(
            //Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        val ungrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (ungrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, ungrantedPermissions, 300)
        }
    }



}


