package com.github.orkest.bluetooth.data

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.github.orkest.bluetooth.domain.BluetoothCommunication
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.MESSAGE_READ
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.MESSAGE_TOAST
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.MESSAGE_WRITE
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.sendErrorToast
import com.github.orkest.bluetooth.domain.Socket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class OrkestBluetoothCommunication(
    private val mmSocket: Socket,
    private val handler: Handler): BluetoothCommunication, Thread(){

    private val mmInStream: InputStream = mmSocket.getInputStream()
    private val mmOutStream: OutputStream = mmSocket.getOutputStream()
    private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

    private val TAG = "OrkestBluetoothCommunication"
    private var stop = false


    override fun run() {
        var numBytes: Int // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            if(stop){
                break
            }

            // Read from the InputStream.
            numBytes = try {
                mmInStream.read(mmBuffer)
            } catch (e: IOException) {
                Log.d(TAG, "Input stream was disconnected", e)

                // Send a failure message back to the activity.
                sendErrorToast("Can't receive data from the other user, please reconnect", handler)
                break
            }

            // Send the obtained bytes to the UI activity.
            if (numBytes > 0) {
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    mmBuffer
                )
                readMsg.sendToTarget()
            }
        }
    }

    override fun sendData(data: ByteArray) {
        try {
            mmOutStream.write(data)
        } catch (e: IOException) {
            Log.e(TAG, "Error occurred when sending data", e)

            // Send a failure message back to the activity.
            return sendErrorToast("Couldn't send data to the other device",handler)

        }

        // Share the sent message with the UI activity.
        val writtenMsg = handler.obtainMessage(
            MESSAGE_WRITE, -1, -1, data)
        writtenMsg.sendToTarget()
    }

    override fun cancel() {
        try {
            stop = true
            this.interrupt()
            mmSocket.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the connect socket", e)
            sendErrorToast("Couldn't close the connection", handler)
        }
    }




}