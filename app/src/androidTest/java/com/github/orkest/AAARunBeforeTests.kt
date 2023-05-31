package com.github.orkest

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters


/**
 * Class to initialize the emulator for all test classes
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AAARunBeforeTests {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setupEmulator() {

            val db = Firebase.firestore
            db.useEmulator("10.0.2.2", 8181)
            db.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }

            val storage = FirebaseStorage.getInstance()
            storage.useEmulator("10.0.2.2", 9199)



        }
    }

    @Test
    fun dummyTest(){

    }
}