package com.github.orkest.bluetooth.domain

import android.bluetooth.BluetoothSocket
import com.google.common.primitives.Bytes

interface BluetoothCommunication: Runnable {

    fun readData(): Bytes

    fun sendData(data: Bytes)

    fun cancel()
}