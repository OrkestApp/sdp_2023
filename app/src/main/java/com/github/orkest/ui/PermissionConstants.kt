package com.github.orkest.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionConstants {

    companion object{
        const val AUDIO_PERMISSION_REQUEST_CODE = 200
        const val CAMERA_PERMISSION_REQUEST_CODE = 300

        /**
         * Returns true if the app has the RECORD_AUDIO permission.
         */
        fun recordPermissionGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * Returns true if the app has the CAMERA permission..
         */
        fun cameraPermissionIsGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }


        /**
         * Requests the RECORD_AUDIO permission.
         * This method shows the "Allow access to the microphone?" dialog.
         */
        fun askRecordPermission(activity: Activity){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf( Manifest.permission.RECORD_AUDIO),
                    AUDIO_PERMISSION_REQUEST_CODE);
            }
        }

        /**
         * Requests the CAMERA permission.
         * This method shows the permission request dialogs.
         */
        fun askCameraPermission(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf( Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }
}