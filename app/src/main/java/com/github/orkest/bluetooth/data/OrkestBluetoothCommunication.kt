package com.github.orkest.bluetooth.data

import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.os.Handler
import android.util.Log
import com.github.orkest.bluetooth.domain.BluetoothCommunication
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.MESSAGE_READ
import com.google.common.primitives.Bytes
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

//TODO: think of how to implement dependency injection for the socket
class OrkestBluetoothCommunication(
                private val mmSocket: BluetoothSocket,
                private val handler: Handler
            ): BluetoothCommunication, Thread(){

    private val mmInStream: InputStream = mmSocket.inputStream
    private val mmOutStream: OutputStream = mmSocket.outputStream
    private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

    override fun run() {
        var numBytes: Int // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            // Read from the InputStream.
            numBytes = try {
                mmInStream.read(mmBuffer)
            } catch (e: IOException) {
                Log.d(TAG, "Input stream was disconnected", e)
                break
            }

            // Send the obtained bytes to the UI activity.
            val readMsg = handler.obtainMessage(
                MESSAGE_READ, numBytes, -1,
                mmBuffer)
            readMsg.sendToTarget()
        }
    }


    override fun readData(): Bytes {
        TODO()
    }

    override fun sendData(data: Bytes) {
        TODO()
    }

    override fun cancel() {
        TODO()
    }


}