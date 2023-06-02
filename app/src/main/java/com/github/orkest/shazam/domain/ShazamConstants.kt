package com.github.orkest.shazam.domain

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.orkest.data.Song

class ShazamConstants {
    companion object{
        const val RECORDING_SAMPLE_RATE: Int  = 48_000
        const val SHAZAM_SESSION_READ_BUFFER_SIZE: Int = 4096

        val SONG_NO_MATCH = Song("No Match", "No Match")

        val SHAZAMING = Song("Shazaming", "Shazaming")

        var SONG_FOUND = SHAZAMING
        val SHAZAM_SESSION_ACTIVE = mutableStateOf(false)
    }
}