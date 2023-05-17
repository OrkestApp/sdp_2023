package com.github.orkest.bluetooth.domain

import android.bluetooth.BluetoothSocket
import com.google.common.primitives.Bytes

/**
 * Interface for communicating with a device over bluetooth, one instance per device
 */
interface BluetoothCommunication: Runnable {

    /**
     * Method to listen for data coming from the socket, in a separate thread
     */
    override fun run()

    /**
     * Method to send data to the socket
     */
    fun sendData(data: ByteArray)

    /**
     * Method to cancel the thread
     */
    fun cancel()
}