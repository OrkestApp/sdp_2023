package com.github.orkest.ViewModel

import androidx.test.platform.app.InstrumentationRegistry
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

class ProfileViewModelTest {
    private val testUser = "testuser"
    private lateinit var path: DocumentReference
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
        path = viewModel.db.collection("user/user-T/users")
            .document(testUser)
            .collection("profile")
            .document("profile_data")

        runBlocking {
                path.set(mapOf(
                    "username" to "Test User",
                    "bio" to "Test bio",
                    "nb_followers" to 10,
                    "nb_followings" to 5,
                    "profile_picture_id" to 1
                ))
                .await()
        }
    }


    @After
    fun tearDown() {
        // Clean up the test data from the Firestore emulator
        runBlocking { path.delete().await() }
    }

    /**
     * Tests if the data is correctly fetched from the database
     */
    @Test
    fun testLoadUserData() {
        viewModel.loadUserData()

        assertEquals("Test User", viewModel.username.value)
        assertEquals("Test bio", viewModel.bio.value)
        assertEquals(10, viewModel.nbFollowers.value)
        assertEquals(5, viewModel.nbFollowings.value)
        assertEquals(1, viewModel.profilePictureId.value)
    }

    /**
     * Tests if the data is correctly updated when values are changed in the database
     */
    @Test
    fun testListenToUserData() {
        viewModel.listenToUserData()

        // Update the Firestore data
        runBlocking {path.update(
                    "username", "New Username",
                    "bio", "New bio",
                    "nb_followers", 20,
                    "nb_followings", 15,
                    "profile_picture_id", 2
                )
                .await()
        }

        assertEquals("New Username", viewModel.username.value)
        assertEquals("New bio", viewModel.bio.value)
        assertEquals(20, viewModel.nbFollowers.value)
        assertEquals(15, viewModel.nbFollowings.value)
        assertEquals(2, viewModel.profilePictureId.value)
    }

}