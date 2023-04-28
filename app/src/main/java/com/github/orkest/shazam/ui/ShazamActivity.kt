package com.github.orkest.shazam.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.orkest.shazam.data.AudioRecognition
import com.github.orkest.shazam.domain.ShazamConstants
import com.github.orkest.ui.theme.OrkestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class ShazamActivity : AppCompatActivity() {
    lateinit var coroutineScope : CoroutineScope

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    coroutineScope = rememberCoroutineScope()
                    coroutineScope.launch {
                        if (!ShazamConstants.recordPermissionGranted(this)
                        ) {
                            askRecordPermission(this)
                        }
                        else AudioRecognition.recordingFlow(coroutineScope)
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ShazamConstants.REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                AudioRecognition.recordingFlow(coroutineScope)
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to record audio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun askRecordPermission(activity: Activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf( Manifest.permission.RECORD_AUDIO),
                ShazamConstants.REQUEST_RECORD_AUDIO_PERMISSION);
        }

    }
}



