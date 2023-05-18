package com.github.orkest.bluetooth.domain

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface BluetoothInterface {

    val devices: MutableList<Device>

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

    /**
     * Asks the user to grant the necessary bluetooth permissions
     */
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

    /**
     * Scans the neighborhood for bluetooth devices and updates the list devices
     */
    fun discoverDevices(context: Context,
                        receiver: BroadcastReceiver,
                        requestBluetooth: ActivityResultLauncher<Intent>)

    /**
     * Connects to the other server device as a client and sends this user's username
     * and receives and sends back the other device's username to the UI
     */
    fun connectToDevice(device: Device)


    /**
     * Starts a server connection waiting for client requests, upon connection, sends this
     * user's username and receives and sends back the client's username to the UI
     */
    fun acceptConnections()

    /**
     * Once the bluetooth sharing is done,
     * must call this function to close all threads and sockets
     */
    fun cancelConnections()


}