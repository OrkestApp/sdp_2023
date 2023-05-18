package com.github.orkest.bluetooth.domain

import android.bluetooth.BluetoothSocket
import java.io.IOException

interface ServerSocket {

    @Throws(IOException::class)
    fun accept(): Socket

    @Throws(IOException::class)
    fun accept(timeout: Int): Socket

    @Throws(IOException::class)
    fun close()
}