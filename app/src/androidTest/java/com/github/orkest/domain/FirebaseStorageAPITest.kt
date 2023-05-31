package com.github.orkest.domain

import android.util.Log
import com.github.orkest.data.Constants
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class FirebaseStorageAPITest {

    /*lateinit var storageAPI: FirebaseStorageAPI

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupEmulator() {
             val emul = FirebaseStorage.getInstance()
            emul.useEmulator("10.0.2.2", 9199)
        }
    }

    @Before
    fun setUp() {
        Constants.CURRENT_LOGGED_USER = "test"
        storageAPI = FirebaseStorageAPI()
    }

    @Test
    fun uploadFetchProfilePicWorksCorrectly() {
        val success = storageAPI.uploadProfilePic(byteArrayOf(0))
        assertTrue(success.get())
        val pic = storageAPI.fetchProfilePic(Constants.CURRENT_LOGGED_USER).get()
        assertTrue(pic.contentEquals("test".toByteArray()))
    }

    @Test
    fun uploadFetchProfilePicThroughUriWorks() {

    }*/
}