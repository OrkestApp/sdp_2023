package com.github.orkest.bluetooth.data

import com.github.orkest.bluetooth.domain.Socket
import java.io.*

class TestSocket(byteArray: ByteArray, private val shouldThrow : Boolean = false) : Socket {


    private val inputStream = if (shouldThrow) PipedInputStream() else ByteArrayInputStream(byteArray)
    private val outputStream = if (shouldThrow) PipedOutputStream() else ByteArrayOutputStream()
    private var connected = true

    override fun getInputStream(): InputStream {
        return inputStream
    }

    override fun getOutputStream(): OutputStream {
        return outputStream
    }

    override fun isConnected(): Boolean {
        return connected
    }

    override fun close() {
        //Close the streams
        try {
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        connected = false

        if (shouldThrow) {
            throw IOException("Test exception")
        }


    }
}