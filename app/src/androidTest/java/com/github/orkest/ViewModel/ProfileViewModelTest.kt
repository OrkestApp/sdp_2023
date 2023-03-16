package com.github.orkest.ViewModel

import androidx.test.platform.app.InstrumentationRegistry
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import com.github.orkest.ViewModel.profile.ProfileViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestoreSettings
import org.junit.BeforeClass

// run firebase emulators:start --only firestore in terminal before
class ProfileViewModelTest {
    private val testUser = "testuser"
    private lateinit var path: DocumentReference
    private lateinit var user: User
    companion object{
        private lateinit var viewModel: ProfileViewModel
        @BeforeClass
        @JvmStatic
        fun setupEmulator(){
            viewModel = ProfileViewModel("testuser")
            viewModel.db.useEmulator("10.0.2.2", 8080)
            viewModel.db.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }

        }
    }
    @Before
    fun setUp() {
        path = viewModel.profileData()
        user = User(profile= Profile("Test User", 1, "Test bio", 10, 5))
        viewModel.userData.set(user)

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
        assertEquals("Test User", user.profile.username)
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
        user = User(profile= Profile("New Username", 2, "New bio", 20, 15))
        viewModel.userData.set(user)

        assertEquals("New Username", user.profile.username)
        assertEquals("New bio", user.profile.bio)
        assertEquals(20, user.profile.nbFollowers)
        assertEquals(15, user.profile.nbFollowings)
        assertEquals(2, user.profile.profilePictureId)
    }
}