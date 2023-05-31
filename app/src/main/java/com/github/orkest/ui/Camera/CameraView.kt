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
import java.util.*
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.domain.FirebaseStorageAPI
import com.github.orkest.ui.PermissionConstants
import com.github.orkest.ui.feed.CreatePost
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.firebase.ktx.Firebase


//This class represents a camera component activity
class CameraView: ComponentActivity(){

    private val viewModel: CameraViewModel = CameraViewModel()

    private val roundedCornerValue = 20.dp
    private val paddingValue = 16.dp

    var hasCameraAccess by Delegates.notNull<Boolean>()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            hasCameraAccess = PermissionConstants.cameraPermissionIsGranted(this)

            // The state of the captured image is kept in a mutableStateOf variable.
            var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
            var capturedVideoUri by remember { mutableStateOf<Uri?>(null) }

            //If an image has been captured
            if(capturedImageUri != null){
                CapturedMedia(capturedUri = capturedImageUri!!, isVideo = false) {
                    capturedImageUri = null
                }
            }
            //If a video has been captured
            if(capturedVideoUri != null){
                CapturedMedia(capturedUri = capturedVideoUri!!, isVideo = true) {
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
            val context = LocalContext.current
            if(hasCameraAccess) {
                //Instance of the camera provider
                val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
                val cameraProvider = cameraProviderFuture.get()

                // Initialize variables for the camera preview and preview view.
                val preview: Preview?
                val previewView: PreviewView = remember { PreviewView(context) }

                //Video variables
                var recording: Recording? = remember { null }
                val videoCapture: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }
                val recordingStarted: MutableState<Boolean> = remember { mutableStateOf(false) }
                lateinit var videoCaptureRecorder:  VideoCapture<Recorder>
                val qualitySelector = QualitySelector.from(
                    Quality.FHD,
                    FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
                )

                // We have chosen the following distribution:
                // selectedMode = true if a video wants to be taken
                // selectedMode = false if a picture wants to be taken
                val selectedMode: MutableState<Boolean> = remember { mutableStateOf(false) }

                //Initializing basic parameters
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

                viewModel.updateCameraPreview(cameraProvider, lifecycleOwner, preview, videoCaptureRecorder, selectedMode)


                //Creates camera preview to allow the user to take a picture or video
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.testTag("Camera Preview")
                )

                Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {
                        //Add button to choose the picture mode
                        SelectCameraModeButton(selectedMode = selectedMode, isVideo = false)
                        //Add button to choose the video mode
                        SelectCameraModeButton(selectedMode = selectedMode, isVideo = true) }

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {
                        if(selectedMode.value){
                            videoCapture.value = videoCaptureRecorder
                            //Add a button to take the video
                            TakeVideoButton(
                                onTakeVideoClick = {
                                    //Check is recording has not already started
                                    if (!recordingStarted.value) {
                                        videoCapture.value?.let { videoCapture ->
                                            recordingStarted.value = true

                                            recording = viewModel.startRecordingVideo(
                                                context = context,
                                                filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                                                videoCapture = videoCapture,
                                                outputDirectory = context.cacheDir,
                                                executor = context.mainExecutor,
                                                audioEnabled = true
                                            ) { event ->
                                                if (event is VideoRecordEvent.Finalize) {
                                                    val uri = event.outputResults.outputUri
                                                    if (uri != Uri.EMPTY) {
                                                        onVideoCaptured(uri)
                                                    } else {
                                                        Log.e(TAG, "Could not record the video", IllegalArgumentException())
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        recordingStarted.value = false
                                        recording?.stop()
                                    }
                                },
                                recordingStarted = recordingStarted)
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
                            if (viewModel.lensFacing == CameraSelector.DEFAULT_BACK_CAMERA) { CameraSelector.DEFAULT_FRONT_CAMERA }
                            else { CameraSelector.DEFAULT_BACK_CAMERA }
                        viewModel.updateCameraPreview(cameraProvider, lifecycleOwner, preview, videoCaptureRecorder, selectedMode)
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
                Text(text= "No camera found on this device. Please allow the app to use the camera in your settings.", modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("No Camera Text"))
            }

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
    fun CapturedMedia(
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
                    modifier = Modifier.fillMaxSize().testTag("Captured Video")
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
                val intent = Intent(context, CreatePost::class.java)
                intent.putExtra("URI", capturedUri.toString())
                intent.putExtra("isVideo", isVideo)
                context.startActivity(intent)
            }, modifier = Modifier
                .padding(paddingValue)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .testTag("Save Button"),
                context = context)
        }
    }

    @Composable
    fun SaveButton(onSaveClick: ()-> Unit, modifier: Modifier, context: Context){
        Button(
            onClick = {
                onSaveClick()
            },
            modifier = modifier,
            shape = RoundedCornerShape(roundedCornerValue),
            colors = ButtonDefaults.buttonColors(
                containerColor = Constants.COLOR_BACKGROUND,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Publish to post", fontWeight = FontWeight.Bold)
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
        recordingStarted:  MutableState<Boolean>){

        Button(
            onClick = onTakeVideoClick,
            modifier = Modifier
                .padding(20.dp)
                .size(100.dp)
                .testTag("Take Video Button"),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
            border = BorderStroke(10.dp, Constants.COLOR_BACKGROUND)
        ) {
            Image(painterResource(if (recordingStarted.value) R.drawable.stop_recording else R.drawable.start_recording),
                contentDescription = "Take Video Button")
        }

    }


    @Composable
    fun SelectCameraModeButton(selectedMode: MutableState<Boolean>, isVideo: Boolean){
        val modeText = if (isVideo) "Video" else "Picture"
        Button(
            onClick = {
                selectedMode.value = !selectedMode.value
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
                    if (selectedMode.value == isVideo) Color.White else Color.Transparent
                )
                .testTag(modeText)
        ) {

            Text(text = modeText)
        }
    }

}


