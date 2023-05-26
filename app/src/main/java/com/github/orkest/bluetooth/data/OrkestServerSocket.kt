package com.github.orkest.bluetooth.data

import android.bluetooth.BluetoothServerSocket
import com.github.orkest.bluetooth.domain.ServerSocket
import com.github.orkest.bluetooth.domain.Socket

class OrkestServerSocket(private val serverSocket: BluetoothServerSocket): ServerSocket {

    override fun accept(): Socket {
        return OrkestClientSocket(serverSocket.accept())
    }

    override fun accept(timeout: Int): Socket {
        return OrkestClientSocket(serverSocket.accept(timeout))
    }

    override fun close() {
        return serverSocket.close()
    }
}