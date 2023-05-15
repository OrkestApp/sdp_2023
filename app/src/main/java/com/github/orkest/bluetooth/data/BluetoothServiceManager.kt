package com.github.orkest.bluetooth.data


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
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
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(receiver, filter)

        if (context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            val bluetoothManager = context.getSystemService(BluetoothManager::class.java);
            if (bluetoothManager != null) {
                val mBleAdapter = bluetoothManager.adapter
                val discoveryStarted = mBleAdapter.startDiscovery()
                Log.d("BluetoothServiceManager", "Discovery started: $discoveryStarted")
            }
        }
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