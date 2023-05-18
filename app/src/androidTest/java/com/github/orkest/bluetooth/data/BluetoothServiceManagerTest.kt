package com.github.orkest.bluetooth.data

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.github.orkest.bluetooth.domain.BluetoothConstants
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class BluetoothServiceManagerTest {

    private var msgReceived = ""
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

                BluetoothConstants.MESSAGE_WRITE -> {
                    // construct a string from the buffer
                    println("Message write")
                    msgReceived = String(msg.obj as ByteArray)
                }
                BluetoothConstants.MESSAGE_READ -> {
                    // construct a string from the valid bytes in the buffer
                    msgReceived = String(msg.obj as ByteArray, 0, msg.arg1)
                }
                BluetoothConstants.MESSAGE_TOAST -> {
                    msgReceived= msg.data.getString("toast").toString()
                }
            }
        }
    }
    private lateinit var bthServiceManager: BluetoothServiceManager

    @Before
    fun setup() {
        bthServiceManager = BluetoothServiceManager(handler)
    }

    //==================TEST-CLIENT-CONNECTION=================
    @Test
    fun connectToDevice() {
    }

    @Test
    fun acceptConnections() {
    }

    @Test
    fun cancelConnections() {
    }
}