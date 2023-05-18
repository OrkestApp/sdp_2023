package com.github.orkest.bluetooth.data

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.github.orkest.bluetooth.domain.BluetoothConstants
import com.github.orkest.bluetooth.domain.BluetoothConstants.Companion.sendErrorToast
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream


class OrkestBluetoothCommunicationTest {

    private lateinit var bthCom: OrkestBluetoothCommunication
    private lateinit var testSocket: TestSocket

    private lateinit var msgReceived: String

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

    @Test
    fun correctlyReceivesData() {
        //Add data :to the inputStream of the socket

        val testMsg:ByteArray = "Username".toByteArray()

        testSocket = TestSocket(testMsg)
        msgReceived = ""
        bthCom = OrkestBluetoothCommunication(testSocket, handler).apply { start() }
        Thread.sleep(2000)
        assertEquals("Username", msgReceived)
        bthCom.cancel()
    }

//

    @Test
    fun correctlySendsDataToOutputStream() {


        testSocket = TestSocket(ByteArray(0))
        msgReceived = ""
        bthCom = OrkestBluetoothCommunication(testSocket, handler)
        bthCom.start()

        val testMsg:ByteArray = "Username".toByteArray()
        bthCom.sendData(testMsg)
        Thread.sleep(2000)
        assertEquals("Username", msgReceived)
        bthCom.cancel()
    }

    @Test
    fun errorToastIsSentCorrectly(){
        testSocket = TestSocket(ByteArray(0))
        msgReceived = ""
        bthCom = OrkestBluetoothCommunication(testSocket, handler)
        bthCom.start()

        sendErrorToast("Error",handler)
        Thread.sleep(2000)
        assertEquals("Error", msgReceived)
        bthCom.cancel()
    }


    @Test
    fun receiveDataAfterStreamClosedFails() {
        //Add data :to the inputStream of the socket
        testSocket = TestSocket(ByteArray(0), true)
        msgReceived = ""
        bthCom = OrkestBluetoothCommunication(testSocket, handler)
        bthCom.start()
        testSocket.getInputStream().close()
        Thread.sleep(2000)
        assertEquals("Can't receive data from the other user, please reconnect", msgReceived)
    }

    @Test
    fun sendDataAfterStreamClosedFails() {
        testSocket = TestSocket(ByteArray(0), true)
        msgReceived = ""
        bthCom = OrkestBluetoothCommunication(testSocket, handler)

        val testMsg:ByteArray = "cantSendTest".toByteArray()
        testSocket.getOutputStream().close()
        Thread.sleep(1000)
        bthCom.sendData(testMsg)
        Thread.sleep(500)
        assertEquals("Couldn't send data to the other device", msgReceived)
    }

    @Test
    fun cancelCorrectlyClosesTheSocket() {
        testSocket = TestSocket(ByteArray(0))
        msgReceived = ""
        bthCom = OrkestBluetoothCommunication(testSocket, handler)
        bthCom.start()

        bthCom.cancel()
        assertFalse(testSocket.isConnected())
    }

    @Test
    fun cancelCorrectlyThrowsException() {
        testSocket = TestSocket(ByteArray(0), true)
        msgReceived = ""
        bthCom = OrkestBluetoothCommunication(testSocket, handler)

        bthCom.cancel()
        Thread.sleep(1000)
        assertEquals( "Couldn't close the connection", msgReceived)
    }
}