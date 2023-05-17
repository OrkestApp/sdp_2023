package com.github.orkest.bluetooth.data


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.github.orkest.bluetooth.domain.BluetoothInterface

class BluetoothServiceManager(private var handler: Handler) : BluetoothInterface {


    override var devices: MutableList<BluetoothDevice> = mutableListOf()


    override fun discoverDevices(
        context: Context,
        receiver: BroadcastReceiver,
        requestBluetooth: ActivityResultLauncher<Intent>
    ) {

        // -------------------------------------------------------------------------------------
        // setup bluetooth
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
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

        devices = pairedDevices?.toMutableList() ?: mutableListOf()

        // ------------------
        // Discover devices
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(receiver, filter)

        if (context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            val mBleAdapter = bluetoothManager.adapter
            mBleAdapter.startDiscovery()
        }

    }

    override fun connectToDevice(device: BluetoothDevice, username: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun acceptConnections(username: ByteArray) {

    }






}