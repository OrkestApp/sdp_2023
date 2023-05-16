package com.github.orkest.bluetooth.data

import android.bluetooth.BluetoothSocket
import com.github.orkest.bluetooth.domain.BluetoothCommunication
import com.google.common.primitives.Bytes
import java.io.InputStream
import java.io.OutputStream
import java.util.logging.Handler

//TODO: think of how to implement dependency injection for the socket
class OrkestBluetoothCommunication(
    private val mmSocket: BluetoothSocket,
    private val handler: Handler
): BluetoothCommunication, Thread(){

    private val mmInStream: InputStream = mmSocket.inputStream
    private val mmOutStream: OutputStream = mmSocket.outputStream
    private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream


    override fun readData(): Bytes {
        TODO()
    }

    override fun sendData(data: Bytes) {
        TODO()
    }

    override fun cancel() {
        TODO()
    }

    override fun run() {
        TODO()
    }
}