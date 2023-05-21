package com.github.orkest.bluetooth.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.github.orkest.bluetooth.domain.Device
import com.github.orkest.bluetooth.domain.Socket
import java.util.*

@SuppressLint("MissingPermission")
class OrkestDevice(private val device: BluetoothDevice): Device {

    override fun createRfcommSocketToServiceRecord(uuid: UUID): Socket {
        return OrkestClientSocket(device.createRfcommSocketToServiceRecord(uuid))
    }

    override fun getAddress(): String {
        return device.address
    }

    override fun getName(): String {
        if (device.name == null)
            return ""
        return device.name
    }


}