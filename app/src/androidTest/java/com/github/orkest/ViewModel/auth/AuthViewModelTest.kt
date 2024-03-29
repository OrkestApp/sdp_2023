package com.github.orkest.ViewModel.auth

import androidx.compose.ui.text.input.TextFieldValue
import com.github.orkest.data.Providers
import com.github.orkest.ui.authentication.AuthViewModel
import org.junit.Before


import org.junit.Test


// Before Launching these tests, you must start the emulator in the command line, using:
// gcloud beta emulators firestore start --host-port=localhost:8080
// To run these tests twice, you must clear the database or restart it before running them again
class AuthViewModelTest {

    private var auth: AuthViewModel = AuthViewModel()

    private val name = "Steve123"
    private val bio = "bio"
    private val provider = Providers.DEEZER


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

    @Test
    fun createWithEmptyUsernameThrowsException() {
        auth.updateUsername(TextFieldValue(""))
        try {
            auth.createUser().get()
        } catch (e: Exception) {
            assert(e.message!!.contains("Username cannot be empty"))
        }
    }
}