package com.github.orkest.bluetooth.domain

import android.os.Bundle
import android.os.Handler
import java.util.*

class BluetoothConstants {
    companion object {
        const val MESSAGE_READ: Int = 0
        const val MESSAGE_WRITE: Int = 1
        const val MESSAGE_TOAST: Int = 2

        val MY_UUID: UUID =  UUID.fromString("a034ad68-d15a-4df8-a2ba-180678ba6853")
        const val NAME: String = "Orkest"

        fun sendErrorToast(error: String, handler: Handler){
            // Send a failure message back to the activity.
            val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
            val bundle = Bundle().apply {
                putString("toast", error)
            }
            writeErrorMsg.data = bundle
            handler.sendMessage(writeErrorMsg)
        }

    }
}