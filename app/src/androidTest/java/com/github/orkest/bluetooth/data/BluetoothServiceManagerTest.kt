package com.github.orkest.bluetooth.data

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.github.orkest.bluetooth.domain.BluetoothConstants
import com.github.orkest.data.Constants
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runners.model.MultipleFailureException.assertEmpty

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
        Constants.CURRENT_LOGGED_USER = "BLUETOOTH_TEST"
        bthServiceManager = BluetoothServiceManager(handler)
    }

    //==================TEST-CLIENT-CONNECTION=================
    @Test
    fun correctlyReceivesData() {
        //Add data :to the inputStream of the socket

        val testMsg:ByteArray = "Received".toByteArray()
        val testDevice = TestDevice(testMsg)

        msgReceived = ""
        bthServiceManager.connectToDevice(testDevice)
        Thread.sleep(1000)
        assertEquals("Received", msgReceived)
    }

    @Test
    fun correctlySendsData() {
        val testMsg:ByteArray = ByteArray(0)
        val testDevice = TestDevice(testMsg)

        msgReceived = ""
        bthServiceManager.connectToDevice(testDevice)
        Thread.sleep(1000)
        assertEquals(Constants.CURRENT_LOGGED_USER, msgReceived)
    }

    @Test
    fun cancelCorrectlyClosesSocket() {
        val testMsg:ByteArray = ByteArray(0)
        val testDevice = TestDevice(testMsg)

        msgReceived = ""
        bthServiceManager.connectToDevice(testDevice)
        Thread.sleep(1000)
        bthServiceManager.cancelConnections()
        assertEquals(false, testDevice.socket.isConnected())
        assertTrue(bthServiceManager.clientConnections.isEmpty())
    }

    //==================TEST-SERVER-CONNECTION=================



    @Test
    fun acceptConnections() {
    }

    @Test
    fun cancelConnections() {
    }
}