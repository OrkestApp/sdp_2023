package com.github.orkest

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters


/**
 * Class to initialize the emulator for all test classes
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RunBeforeTests {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setupEmulator() {

            val db = Firebase.firestore
            db.useEmulator("10.0.2.2", 8080)
            db.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }

        }
    }

    @Test
    fun dummyTest(){

    }
}