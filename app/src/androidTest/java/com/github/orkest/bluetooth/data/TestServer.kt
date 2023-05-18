package com.github.orkest.bluetooth.data

import com.github.orkest.bluetooth.domain.ServerSocket
import com.github.orkest.bluetooth.domain.Socket
import okio.IOException

class TestServer(val msg : ByteArray,val shouldThrow: Boolean = false,
                 val onlyCancelFails: Boolean = false): ServerSocket {

    lateinit var testSocket : TestSocket
    override fun accept(): Socket {
        if (shouldThrow) {
            throw IOException("Test exception")
        }
        testSocket = TestSocket(msg, shouldThrow)
        return testSocket
    }

    override fun accept(timeout: Int): Socket {
        return accept()
    }

    override fun close() {

        if (onlyCancelFails) {
            throw IOException("Test exception")
        }
        if (!shouldThrow) testSocket.close()
    }

}