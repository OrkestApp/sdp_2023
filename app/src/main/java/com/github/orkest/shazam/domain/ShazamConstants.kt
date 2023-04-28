package com.github.orkest.shazam.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class ShazamConstants {
    companion object{
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        const val RECORDING_SAMPLE_RATE: Int  = 48_000
        const val SHAZAM_SESSION_READ_BUFFER_SIZE: Int = 4096

        fun recordPermissionGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}