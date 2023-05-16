package com.github.orkest.bluetooth.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.github.orkest.bluetooth.data.BluetoothServiceManager
import com.github.orkest.bluetooth.ui.ui.theme.OrkestTheme

class BluetoothActivity : ComponentActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothServiceManager: BluetoothServiceManager

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            Log.d("BluetoothActivity", "Bluetooth granted")
        } else {
            //deny
            Log.d("BluetoothActivity", "Bluetooth denied")
        }
    }

    private var discovery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        // discoverable
        Log.d("BluetoothActivity", "Bluetooth adapter: $bluetoothAdapter")
        bluetoothServiceManager.discoverDevices(bluetoothAdapter, this, receiver)
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            intent.action?.let { Log.d("BluetoothActivity", it) }
            when(intent.action) {

                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("BluetoothActivity", "BluetoothDevice.ACTION_FOUND")
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        return
                    } else {
                        device?.name
                        Log.d("BluetoothActivity", "Device name: ${device?.name}")
                    }
                    val deviceHardwareAddress = device?.address // MAC address
                    }
                }
            }
        }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // -------------------------------------------------------------------------------------
        // setup bluetooth

        bluetoothServiceManager = BluetoothServiceManager()
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter


        // check for permissions
        val permissions = bluetoothServiceManager.checkPermissionGranted(this)
        if (!permissions) {
            bluetoothServiceManager.askBluetoothPermission(this)
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        discovery.launch(discoverableIntent)

        // -------------------------------------------------------------------------------------

        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                }
            }
        }
    }
}

