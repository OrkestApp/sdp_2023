package com.github.orkest.bluetooth.data


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import com.github.orkest.bluetooth.domain.BluetoothInterface

class BluetoothServiceManager : BluetoothInterface {

    override fun discoverDevices(bluetoothAdapter: BluetoothAdapter, context: Context, receiver: BroadcastReceiver) {

        // start by querying paired devices
        val pairedDevices: MutableSet<BluetoothDevice>? = if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("BluetoothServiceManager", "Bluetooth permission not granted")
            return
        } else {
            bluetoothAdapter.bondedDevices
        }

        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
        }
        Log.d("BluetoothServiceManager", "Paired devices: $pairedDevices")

        // ------------------
        // Discover devices
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(context, receiver, filter, RECEIVER_EXPORTED)




    }

    override fun connectDevice() {
        TODO("Not yet implemented")
    }

    override fun pairDevice() {
        TODO("Not yet implemented")
    }

    override fun sendData(data: String) {
        TODO("Not yet implemented")
    }


}