package com.github.orkest.bluetooth.domain

import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream

/**
 *  Adapter interface for the BluetoothSocket
 */
interface Socket: Closeable {

    fun getInputStream() : InputStream

    fun getOutputStream() : OutputStream

    fun isConnected() : Boolean

}