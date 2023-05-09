package com.github.orkest.bluetooth.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.github.orkest.bluetooth.data.BluetoothServiceManager
import com.github.orkest.bluetooth.domain.BluetoothInterface
import com.github.orkest.bluetooth.ui.ui.theme.OrkestTheme

class BluetoothActivity : ComponentActivity() {

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
            Log.d("BluetoothActivity", "Bluetooth granted")
        } else {
            //deny
            Log.d("BluetoothActivity", "Bluetooth denied")
        }
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
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
                    }
                    val deviceHardwareAddress = device?.address // MAC address
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // -------------------------------------------------------------------------------------
        // setup bluetooth

        val bluetoothServiceManager = BluetoothServiceManager()

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.e("BluetoothActivity", "Device doesn't support Bluetooth")
        }
//        if (bluetoothAdapter?.isEnabled == false) {
        Log.d("BluetoothActivity", "Bluetooth enabled")
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestBluetooth.launch(enableBtIntent)
//        }

        if (bluetoothAdapter != null) {

            bluetoothServiceManager.discoverDevices(bluetoothAdapter, this, receiver)
        }

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

