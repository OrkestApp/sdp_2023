package com.github.orkest.bluetooth.data

import android.bluetooth.BluetoothSocket
import com.github.orkest.bluetooth.domain.Socket
import java.io.InputStream
import java.io.OutputStream

class OrkestSocket(private var bluetoothSocket: BluetoothSocket): Socket{

    override fun getInputStream(): InputStream {
        return bluetoothSocket.inputStream
    }

    override fun getOutputStream(): OutputStream {
        return bluetoothSocket.outputStream
    }

    override fun isConnected(): Boolean {
        return bluetoothSocket.isConnected
    }

    override fun close() {
        return bluetoothSocket.close()
    }
}