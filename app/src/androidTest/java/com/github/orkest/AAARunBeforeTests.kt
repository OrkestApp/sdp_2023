package com.github.orkest

import androidx.test.rule.GrantPermissionRule
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters


/**
 * Class to initialize the emulator for all test classes
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AAARunBeforeTests {

    //add recording permissions once for all tests
    @get:Rule
    var permissionAudio: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

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