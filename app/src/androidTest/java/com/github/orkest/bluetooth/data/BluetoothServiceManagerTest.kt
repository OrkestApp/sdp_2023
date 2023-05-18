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
    private var msgSent = ""
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {

                BluetoothConstants.MESSAGE_WRITE -> {
                    // construct a string from the buffer
                    println("Message write")
                    msgSent = String(msg.obj as ByteArray)
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
        msgReceived = ""
        msgSent = ""
    }

    //==================TEST-CLIENT-CONNECTION=================
    @Test
    fun correctlyReceivesAndSendsData() {
        //Add data :to the inputStream of the socket

        val testMsg:ByteArray = "Received".toByteArray()
        val testDevice = TestDevice(testMsg)

        msgReceived = ""
        bthServiceManager.connectToDevice(testDevice)
        Thread.sleep(2000)
        assertEquals("Received", msgReceived)
        assertEquals(Constants.CURRENT_LOGGED_USER, msgSent)
        bthServiceManager.cancelConnections()
    }


    @Test
    fun cancelCorrectlyClosesSocket() {
        val testMsg:ByteArray = ByteArray(0)
        val testDevice = TestDevice(testMsg)

        msgReceived = ""
        bthServiceManager.connectToDevice(testDevice)
        Thread.sleep(2000)
        bthServiceManager.cancelConnections()
        assertEquals(false, testDevice.socket.isConnected())
        assertTrue(bthServiceManager.clientConnections.isEmpty())

    }

    //==================TEST-SERVER-CONNECTION=================


    @Test
    fun correctlyReceivesAndSendsDataServer() {
        val testMsg:ByteArray = "Received".toByteArray()
        val testDevice = TestDevice(testMsg)
        val socket = TestServer(testMsg)

        msgReceived = ""
        msgSent = ""
        val thread = bthServiceManager.AcceptThread(socket)
        thread.start()
        Thread.sleep(2000)
        assertEquals("Received", msgReceived)
        assertEquals(Constants.CURRENT_LOGGED_USER, msgSent)
        thread.cancel()
        thread.interrupt()
    }

    @Test
    fun cancelCorrectlyClosesServerSocket() {
        val testMsg:ByteArray = ByteArray(0)
        val socket = TestServer(testMsg)

        msgReceived = ""
        val thread = bthServiceManager.AcceptThread(socket)
        bthServiceManager.serverConnection = thread
        thread.start()
        Thread.sleep(2000)
        bthServiceManager.cancelConnections()
        assertEquals(false, socket.testSocket.isConnected())
        assertTrue(bthServiceManager.serverConnection == null)
        thread.cancel()
    }

    @Test
    fun cancelFailsThrows(){
        val testMsg:ByteArray = ByteArray(0)
        val socket = TestServer(testMsg,false, true)

        msgReceived = ""
        val thread = bthServiceManager.AcceptThread(socket)
        bthServiceManager.serverConnection = thread
        thread.start()
        Thread.sleep(2000)
        bthServiceManager.cancelConnections()
        Thread.sleep(2000)
        assertEquals(false, socket.testSocket.isConnected())
        assertTrue(bthServiceManager.serverConnection == null)
        assertEquals("Could not close the server socket", msgReceived)
        thread.cancel()
    }

    @Test
    fun acceptFailsThrows(){

        val testMsg:ByteArray = ByteArray(0)
        val socket = TestServer(testMsg,true, false)

        msgReceived = ""
        val thread = bthServiceManager.AcceptThread(socket)
        bthServiceManager.serverConnection = thread
        thread.start()
        Thread.sleep(2000)
        assertEquals("Socket's accept method failed", msgReceived)
        thread.interrupt()
    }
}