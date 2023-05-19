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
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import com.github.orkest.bluetooth.domain.*
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.MY_UUID
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.NAME
import com.github.orkest.data.Constants
import java.io.IOException

class BluetoothServiceManager(private var handler: Handler) : BluetoothInterface {


    override var devices: MutableList<Device> = mutableListOf()
    var list = mutableStateListOf<Device>()
    val clientConnections: MutableList<ConnectThread> = mutableListOf()
    var serverConnection : AcceptThread? = null

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

        if (pairedDevices != null) {
            devices = pairedDevices.map {
                OrkestDevice(it)
            }.toMutableList()
        }

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
    override fun connectToDevice(device: Device) {
        val clientSocket: Socket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }
        Log.d(TAG,"THis is the socket + ${clientSocket.toString()}")
        val thread = ConnectThread(clientSocket)
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
        clientConnections.clear()
        serverConnection?.cancel()
        serverConnection = null
    }

    override fun addDevice(device: Device) {
        val newDevices = devices.toMutableList()
        newDevices.add(device)
        devices = newDevices
    }


    //=======================================THREADS======================================

    /**
     *
     */
    @SuppressLint("MissingPermission")
    inner class AcceptThread(private val serverSocket: ServerSocket?) : Thread() {

        private val communications: MutableList<BluetoothCommunication> = mutableListOf()

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: Socket? = try {
                    serverSocket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    BluetoothConstants.sendErrorToast("Socket's accept method failed",handler)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    val comm = OrkestBluetoothCommunication(it, handler).apply {
                        Log.d(TAG, "Server socket accepted")
                        start()
                        sendData(username)
                        Log.d(TAG, "Server socket sent username")
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
                BluetoothConstants.sendErrorToast("Could not close the server socket",handler)
            }
        }
    }


    /**
     * Thread class to connect to other server devices
     * Acts as the client
     */
    @SuppressLint("MissingPermission")
    inner class ConnectThread(private val socket: Socket?) : Thread() {


        private lateinit var communication: BluetoothCommunication

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            // bluetoothAdapter.cancelDiscovery()

            socket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()
                // The connection attempt succeeded.
                // Create the connected thread to transfer data
                communication = OrkestBluetoothCommunication(socket, handler)
                //Start the connected thread to receive the username of the other device
                communication.start()
                // Send the username of the current device to the other device
                communication.sendData(username)
                Log.d(TAG, "Client socket sent username")
            }
        }

        fun cancel() {
            communication.cancel()
            this.interrupt()
        }
    }
}