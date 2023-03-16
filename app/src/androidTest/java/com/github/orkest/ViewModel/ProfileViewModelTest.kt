package com.github.orkest.ViewModel

import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.github.orkest.ViewModel.profile.ProfileViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.BeforeClass

// run firebase emulators:start --only firestore in terminal before
class ProfileViewModelTest {

    private var testUserName: String = "testUser"
    private lateinit var path: DocumentReference
    private lateinit var user: User

    companion object{
        private lateinit var viewModel: ProfileViewModel
        @BeforeClass
        @JvmStatic
        fun setupEmulator(){
            viewModel = ProfileViewModel("testUser")
            try {
                viewModel.db.useEmulator("10.0.2.2", 8080)
                viewModel.db.firestoreSettings = firestoreSettings {
                    isPersistenceEnabled = false
                }
            } catch(_: IllegalStateException){ }

        }
    }
    @Before
    fun setUp() {
        path = viewModel.userDocument(testUserName)
        runBlocking {
            user = User(profile = Profile(testUserName, 1, "Test bio", 10, 5))
            viewModel.userDocument(testUserName).set(user).await()
        }
        viewModel.setupListener()
    }

    /**
    @After
    fun tearDown() {
        // Clean up the test data from the Firestore emulator
        runBlocking { path.delete().await() }
    }**/

    /**
     * Tests if the data is correctly fetched from the database
     */

    @Test
    fun testLoadUserData() {
        assertEquals(testUserName, user.profile.username)
        assertEquals("Test bio", user.profile.bio)
        assertEquals(10, user.profile.nbFollowers)
        assertEquals(5, user.profile.nbFollowings)
        assertEquals(1, user.profile.profilePictureId)
    }

    /**
     * Tests if the data is correctly updated when values are changed in the database
     */
    @Test
    fun testListenToUserData() {
        val newUsername = "newUsername"
        runBlocking {
            user = User(profile = Profile(newUsername, 2, "New bio", 20, 15))
            viewModel.userDocument(newUsername).set(user).await()
        }

        assertEquals(newUsername, user.profile.username)
        assertEquals("New bio", user.profile.bio)
        assertEquals(20, user.profile.nbFollowers)
        assertEquals(15, user.profile.nbFollowings)
        assertEquals(2, user.profile.profilePictureId)
    }

    //if a user changes their username, on a besoin de le supprimer de user_LETTRE
    // A faire avec roman
    // Quand on change spécifiquement le username, alors on applique une méthode précise
    //Pas moi qui m'occupe de ça though
}