package com.github.orkest.bluetooth.domain

import java.util.*

class BluetoothConstants {
    companion object {
        const val MESSAGE_READ: Int = 0
        const val MESSAGE_WRITE: Int = 1
        const val MESSAGE_TOAST: Int = 2

        val MY_UUID: UUID =  UUID.fromString("a034ad68-d15a-4df8-a2ba-180678ba6853")
        const val NAME: String = "Orkest"

    }
}