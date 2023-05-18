package com.github.orkest.bluetooth.data


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.github.orkest.bluetooth.domain.BluetoothCommunication
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.MY_UUID
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.NAME
import com.github.orkest.bluetooth.domain.BluetoothInterface
import com.github.orkest.bluetooth.domain.ServerSocket
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

    @SuppressLint("MissingPermission")
    override fun connectToDevice(device: BluetoothDevice) {
        val clientSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }
        val thread = ConnectThread(clientSocket?.let { OrkestClientSocket(it) })
        clientConnections.add(thread)
        thread.start()
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    override fun acceptConnections() {
        val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID)
        }
        val thread = AcceptThread(mmServerSocket?.let { OrkestServerSocket(it) })
        serverConnection = thread
        thread.start()
    }


    override fun cancelConnections() {
        clientConnections.forEach {
            it.cancel()
        }
        serverConnection?.cancel()
    }


    //=======================================THREADS======================================

    /**
     *
     */
    @SuppressLint("MissingPermission")
    private inner class AcceptThread(private val serverSocket: ServerSocket?) : Thread() {

        private val communications: MutableList<BluetoothCommunication> = mutableListOf()

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: Socket? = try {
                    serverSocket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    //TODO: handle cancellation of this connection
                    val comm = OrkestBluetoothCommunication(it, handler).apply {
                        start()
                        sendData(username)
                    }
                    communications.add(comm)
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                serverSocket?.close()
                communications.forEach {
                    it.cancel()
                }
                this.interrupt()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }


    /**
     * Thread class to connect to other server devices
     * Acts as the client
     */
    @SuppressLint("MissingPermission")
    private inner class ConnectThread(private val socket: Socket?) : Thread() {


        private lateinit var communication: BluetoothCommunication

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            socket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded.
                // Create the connected thread to transfer data
                //TODO: handle cancellation of this connection
                communication = OrkestBluetoothCommunication(socket,
                                                                handler)
                //Start the connected thread to receive the username of the other device
                communication.start()
                // Send the username of the current device to the other device
                communication.sendData(username)
            }
        }

        fun cancel() {
            try {
                socket?.close()
                communication.cancel()
                this.interrupt()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}