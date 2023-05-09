package com.github.orkest.bluetooth.domain

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context

interface BluetoothInterface {

    fun discoverDevices(bluetoothAdapter: BluetoothAdapter, context: Context, receiver: BroadcastReceiver)

    fun connectDevice()

    fun pairDevice()

    fun sendData(data: String)
}