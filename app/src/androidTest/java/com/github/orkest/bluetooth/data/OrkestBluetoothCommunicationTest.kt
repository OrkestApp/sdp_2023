package com.github.orkest.bluetooth.data

import android.R
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import com.github.orkest.bluetooth.domain.BluetoothConstants
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class OrkestBluetoothCommunicationTest {

    lateinit var bthCom: OrkestBluetoothCommunication
    lateinit var testSocket: TestSocket
    lateinit var handler: Handler
    private lateinit var msgReceived: String

    @Before
    fun setUp() {
        testSocket = TestSocket()
        msgReceived = ""
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {

                    BluetoothConstants.MESSAGE_WRITE -> {
                        // construct a string from the buffer
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
        bthCom = OrkestBluetoothCommunication(testSocket, handler)
        bthCom.start()
    }

    @Test
    fun correctlyReceivesData() {
        //Add data to the inputStream of the socket
        testSocket.getInputStream().
    }

    @Test
    fun sendData() {
    }

    @Test
    fun cancel() {
    }
}