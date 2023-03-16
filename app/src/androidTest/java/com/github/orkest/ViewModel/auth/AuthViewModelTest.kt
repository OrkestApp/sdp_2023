package com.github.orkest.ViewModel.auth

import androidx.compose.ui.text.input.TextFieldValue
import com.github.orkest.Model.Providers
import com.google.firebase.firestore.ktx.firestoreSettings
import org.junit.Before

import org.junit.BeforeClass

import org.junit.Test


// Before Launching these tests, you must start the emulator in the command line, using:
// gcloud beta emulators firestore start --host-port=localhost:8080
// To run these tests twice, you must clear the database or restart it before running them again
class AuthViewModelTest {

    companion object {
        private lateinit var auth: AuthViewModel

        private const val name = "Steve123"
        private const val bio = "bio"
        private val provider = Providers.DEEZER

        @BeforeClass
        @JvmStatic
        fun setupEmulator() {
            auth = AuthViewModel()
            try {
                auth.db.useEmulator("10.0.2.2", 8080)
                auth.db.firestoreSettings = firestoreSettings {
                    isPersistenceEnabled = false
                }
            }
            catch (_: java.lang.IllegalStateException){

            }


        }
    }

    @Before
    fun setup(){
        auth.updateUsername(TextFieldValue(name))
        auth.updateBio(TextFieldValue(bio))
        auth.updateProvider(provider)
    }

    @Test
    fun usernameUpdatesCorrectly() {
        assert(auth.getUsername().text == name)
    }

    @Test
    fun bioUpdatesCorrectly() {
        assert(auth.getBio().text == bio)
    }

    @Test
    fun providerUpdatesCorrectly() {
        assert(auth.getProvider() == provider)
    }

    @Test
    fun createValidUserReturnsTrue() {
        auth.updateUsername(TextFieldValue("Valid"))
        assert(auth.createUser().get() == true)
    }

    @Test
    fun createInvalidUserReturnsFalse() {
        auth.updateUsername(TextFieldValue("Invalid"))
        assert(auth.createUser().get() == true)
        //Adding 2 times the same user should return false
        assert(auth.createUser().get() == false)
    }
}