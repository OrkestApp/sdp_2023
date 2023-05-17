package com.github.orkest.bluetooth.data


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.github.orkest.bluetooth.domain.BluetoothCommunication
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.MY_UUID
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.NAME
import com.github.orkest.bluetooth.domain.BluetoothInterface
import com.github.orkest.bluetooth.domain.Socket
import com.github.orkest.data.Constants
import java.io.IOException

class BluetoothServiceManager(private var handler: Handler) : BluetoothInterface {


    override var devices: MutableList<BluetoothDevice> = mutableListOf()
    private val clientConnections: MutableList<ConnectThread> = mutableListOf()
    private var serverConnection : AcceptThread? = null

    val username: ByteArray = Constants.CURRENT_LOGGED_USER.toByteArray()

    lateinit var bluetoothAdapter : BluetoothAdapter
    val TAG = "BluetoothServiceManager"


    override fun discoverDevices(
        context: Context,
        receiver: BroadcastReceiver,
        requestBluetooth: ActivityResultLauncher<Intent>
    ) {

        // -------------------------------------------------------------------------------------
        // setup bluetooth
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

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

    override fun connectToDevice(device: BluetoothDevice) {
        val thread = ConnectThread(device)
        clientConnections.add(thread)
        thread.start()
    }

    override fun acceptConnections() {
        val thread = AcceptThread()
        serverConnection = thread
        thread.start()
    }

    override fun cancelConnections() {
        clientConnections.forEach {
            it.cancel()
            it.interrupt()
        }
        serverConnection?.cancel()
        serverConnection?.interrupt()
    }


    @SuppressLint("MissingPermission")
    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID)
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    //TODO: handle cancellation of this connection
                    OrkestBluetoothCommunication(OrkestSocket(it), handler).apply {
                        start()
                        sendData(username)
                    }
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded.
                // Create the connected thread to transfer data
                //TODO: handle cancellation of this connection
                val connection = OrkestBluetoothCommunication(OrkestSocket(socket),
                                                                handler)
                //Start the connected thread to receive the username of the other device
                connection.start()
                // Send the username of the current device to the other device
                connection.sendData(username)
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}