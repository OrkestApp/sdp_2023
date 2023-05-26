package com.github.orkest.bluetooth.data

import com.github.orkest.bluetooth.domain.Device
import com.github.orkest.bluetooth.domain.Socket
import java.net.Inet4Address
import java.util.*

class TestDeviceUI(val deviceName: String, val addr: String = "test"): Device {
    override fun createRfcommSocketToServiceRecord(uuid: UUID): Socket {
        TODO("Not yet implemented")
    }

    override fun getAddress(): String {
        return addr
    }

    override fun getName(): String {
        return deviceName
    }

}