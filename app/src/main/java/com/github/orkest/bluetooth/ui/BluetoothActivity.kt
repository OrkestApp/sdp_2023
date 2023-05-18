package com.github.orkest.bluetooth.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.github.orkest.bluetooth.data.BluetoothServiceManager
import com.github.orkest.bluetooth.domain.BluetoothInterface
import com.github.orkest.bluetooth.ui.ui.theme.OrkestTheme

class BluetoothActivity() : ComponentActivity() {

    private var bluetoothServiceManager: BluetoothInterface? = null

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            Log.d("BluetoothActivity", "Bluetooth granted")
        } else {
            //deny
            Log.d("BluetoothActivity", "Bluetooth denied")
        }
    }

    var discovery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->

        // discoverable
        bluetoothServiceManager?.discoverDevices(this, receiver, requestBluetooth)
    }


    fun onReceiveHandle(intent: Intent, bluetoothServiceManager: BluetoothInterface? = this.bluetoothServiceManager) {
        intent.action?.let { Log.d("BluetoothActivity", it) }
        when(intent.action) {

            BluetoothDevice.ACTION_FOUND -> {
                Log.d("BluetoothActivity", "BluetoothDevice.ACTION_FOUND")
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                } else {
                    if (device != null) {
                        bluetoothServiceManager?.addDevice(device.name, device.address)
                    }
                    Log.d("BluetoothActivity", "Device name: ${device?.name}")
                }
            }
        }
    }

    fun setServiceManager(manager: BluetoothInterface) {
        bluetoothServiceManager = manager
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            intent.action?.let { Log.d("BluetoothActivity", it) }
            onReceiveHandle(intent)
            }
        }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // -------------------------------------------------------------------------------------

        setContent {
            OrkestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BluetoothActivityStart(activity = this)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BluetoothActivityStart(bluetoothServiceManager : BluetoothInterface = BluetoothServiceManager(), activity: BluetoothActivity){
    // check for permissions
    val permissions = bluetoothServiceManager.checkPermissionGranted(activity)
    if (!permissions) {
        bluetoothServiceManager.askBluetoothPermission(activity)
    }
    val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
    }
    activity.setServiceManager(bluetoothServiceManager)
    activity.discovery.launch(discoverableIntent)
}



