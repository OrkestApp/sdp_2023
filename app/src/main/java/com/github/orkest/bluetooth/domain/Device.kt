package com.github.orkest.bluetooth.domain

import java.util.*

interface Device {

    fun createRfcommSocketToServiceRecord(uuid: UUID): Socket

    fun getAddress(): String

    fun getName(): String
}