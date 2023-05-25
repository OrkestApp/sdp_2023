package com.github.orkest.domain

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

//    companion object {
//        @BeforeClass
//        @JvmStatic
//        fun setupEmulator() {
//            val storage = FirebaseStorage.getInstance()
//            storage.useEmulator("10.0.2.2", 9199)
//        }
//    }

    lateinit var storage: FirebaseStorageAPI

    @Before
    fun setUp() {
        Constants.CURRENT_LOGGED_USER = "test"
        storage = FirebaseStorageAPI()
    }

    @Test
    fun uploadFetchProfilePicWorksCorrectly() {
        val success = storage.uploadProfilePic("test".toByteArray()).get()
        assertTrue(success)
        val pic = storage.fetchProfilePic(Constants.CURRENT_LOGGED_USER).get()
        assertTrue(pic.contentEquals("test".toByteArray()))
    }

    @Test
    fun uploadFetchProfilePicThroughUriWorks() {

    }
}