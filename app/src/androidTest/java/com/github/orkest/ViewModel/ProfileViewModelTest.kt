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
    private var testBio = "This is my bio"
    private var testNbFollowers = 10
    private var testNbFollowings = 2
    private var testProfilePicture = 45

    private lateinit var user: User
    private var viewModel: ProfileViewModel = ProfileViewModel(testUserName)


    @Before
    fun setUp() {
        runBlocking {
            user = User(profile = Profile(testUserName, testProfilePicture, testBio, testNbFollowers, testNbFollowings))
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
        assertEquals(testBio, user.profile.bio)
        assertEquals(testNbFollowers, user.profile.nbFollowers)
        assertEquals(testNbFollowings, user.profile.nbFollowings)
        assertEquals(testProfilePicture, user.profile.profilePictureId)
    }

    /**
     * Tests if the data is correctly updated when values are changed in the database
     */
    @Test
    fun testListenToUserData() {
        val newUsername = "newUsername"
        runBlocking {
            user = User(profile = Profile(newUsername, 2, "New bio", 20, 15))
            viewModel.userDocument(testUserName).set(user).await()
        }

        assertEquals(newUsername, user.profile.username)
        assertEquals("New bio", user.profile.bio)
        assertEquals(20, user.profile.nbFollowers)
        assertEquals(15, user.profile.nbFollowings)
        assertEquals(2, user.profile.profilePictureId)
    }

    @Test
    fun onlyProfilePictureIdGetsUpdated(){
        val newProfilePicture = 928
        runBlocking {
            user.profile.profilePictureId = newProfilePicture
            viewModel.userDocument(testUserName).set(user).await()
        }
        assertEquals(newProfilePicture, user.profile.profilePictureId)
        assertEquals(testUserName, user.profile.username)
        assertEquals(testBio, user.profile.bio)
        assertEquals(testNbFollowers, user.profile.nbFollowers)
        assertEquals(testNbFollowings, user.profile.nbFollowings)
    }

    @Test
    fun onlyBioGetsUpdated(){
        val newBio = "My new bio"
        runBlocking {
            user.profile.bio = newBio
            viewModel.userDocument(testUserName).set(user).await()
        }
        assertEquals(newBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(testNbFollowers, user.profile.nbFollowers)
        assertEquals(testNbFollowings, user.profile.nbFollowings)
        assertEquals(testProfilePicture, user.profile.profilePictureId)
    }

    @Test
    fun onlyNbFollowersGetsUpdated(){
        val newNbFollowers = 32
        runBlocking {
            user.profile.nbFollowers = newNbFollowers
            viewModel.userDocument(testUserName).set(user).await()
        }
        assertEquals(testBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(newNbFollowers, user.profile.nbFollowers)
        assertEquals(testNbFollowings, user.profile.nbFollowings)
        assertEquals(testProfilePicture, user.profile.profilePictureId)
    }

    @Test
    fun onlyNbFollowingsGetsUpdated(){
        val newNbFollowings = 0
        runBlocking {
            user.profile.nbFollowings = newNbFollowings
            viewModel.userDocument(testUserName).set(user).await()
        }
        assertEquals(testBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(testNbFollowers, user.profile.nbFollowers)
        assertEquals(newNbFollowings, user.profile.nbFollowings)
        assertEquals(testProfilePicture, user.profile.profilePictureId)
    }

    @Test
    fun testUserDocument() {
        val documentReference = viewModel.userDocument("testUser")
        assertEquals(documentReference.path, "user/user-T/users/testUser")
    }

    /**
     * If a user changes their username, the path will change as well.
     * The ProfileViewModel will have to be called with the new username as a parameter
     */
    @Test
    fun onlyUsernameChanges(){
        val newUsername = "newUsername"
        val newViewModel = ProfileViewModel(newUsername)
        runBlocking {
            user = User(profile = Profile(newUsername, 2, "New bio", 20, 15))
            newViewModel.userDocument(newUsername).set(user).await()
        }
        newViewModel.setupListener()

        assertEquals(newUsername, user.profile.username)
        assertEquals("New bio", user.profile.bio)
        assertEquals(20, user.profile.nbFollowers)
        assertEquals(15, user.profile.nbFollowings)
        assertEquals(2, user.profile.profilePictureId)
    }





    //if a user changes their username, on a besoin de le supprimer de user_LETTRE et le mettre dans user_NOUVELLE_LETTRE

}