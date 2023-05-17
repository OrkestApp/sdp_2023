package com.github.orkest.bluetooth.data

import com.github.orkest.bluetooth.domain.Socket
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

class TestSocket : Socket {

    private val inputStream = ByteArrayInputStream(ByteArray(0))
    private val outputStream = ByteArrayOutputStream()

    override fun getInputStream(): InputStream {
        return inputStream
    }

    override fun getOutputStream(): OutputStream {
        return outputStream
    }

    override fun isConnected(): Boolean {
        return true
    }

    override fun close() {
        //Close the streams
        inputStream.close()
        outputStream.close()
    }
}