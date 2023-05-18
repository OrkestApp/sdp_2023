package com.github.orkest.bluetooth.data

import com.github.orkest.bluetooth.domain.Device
import com.github.orkest.bluetooth.domain.Socket
import java.util.*

class TestDevice(val byteArray: ByteArray, val shouldThrow: Boolean = false): Device {

        lateinit var  socket: Socket
        override fun createRfcommSocketToServiceRecord(uuid: UUID): Socket {
            socket = TestSocket(byteArray, shouldThrow)
            return socket
        }

        override fun getAddress(): String {
            return "Test address"
        }

        override fun getName(): String {
            return "Test name"
        }

}