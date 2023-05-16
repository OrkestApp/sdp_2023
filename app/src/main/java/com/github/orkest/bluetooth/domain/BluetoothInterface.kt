package com.github.orkest.bluetooth.domain

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface BluetoothInterface {

    /*
     * Check if bluetooth permissions are granted
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun checkPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun askBluetoothPermission(activity: Activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION),
                0);
        }

    }

    fun discoverDevices(context: Context,
                        receiver: BroadcastReceiver,
                        requestBluetooth: ActivityResultLauncher<Intent>
    )

    fun connectDevice()

    fun pairDevice()

    fun sendData(data: String)
}