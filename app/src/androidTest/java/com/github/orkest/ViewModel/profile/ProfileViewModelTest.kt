package com.github.orkest.ViewModel.profile

import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test

// run firebase emulators:start --only firestore in terminal before
class ProfileViewModelTest {

    private var testUserName = "testUser"
    private var testBio = "This is my bio"
    private var testNbFollowers = 10
    private var testNbFollowings = 2
    private var testProfilePicture = 45

    private val newUsername = "newUsername"
    private val newBio = "My new bio"
    private val newNbFollowers = 32
    private val newNbFollowings = 0
    private val newProfilePicture = 928

    private lateinit var user: User
    private var viewModel: ProfileViewModel = ProfileViewModel(testUserName)

    @Before
    fun setUp() {
        runBlocking {
            user = User(username = testUserName, profile = Profile(testUserName, testProfilePicture, testBio, testNbFollowers, testNbFollowings))

            withContext(Dispatchers.IO) {
                FireStoreDatabaseAPI().addUserInDatabase(user).get()
            }



        }
        viewModel.setupListener()
    }


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
    fun onlyProfilePictureIdGetsUpdated(){
        runBlocking {
            user.profile.profilePictureId = newProfilePicture

        }
        assertEquals(newProfilePicture, user.profile.profilePictureId)
        assertEquals(testUserName, user.profile.username)
        assertEquals(testBio, user.profile.bio)
        assertEquals(testNbFollowers, user.profile.nbFollowers)
        assertEquals(testNbFollowings, user.profile.nbFollowings)
    }

    @Test
    fun onlyBioGetsUpdated(){
        runBlocking {
            user.profile.bio = newBio

        }
        assertEquals(newBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(testNbFollowers, user.profile.nbFollowers)
        assertEquals(testNbFollowings, user.profile.nbFollowings)
        assertEquals(testProfilePicture, user.profile.profilePictureId)
    }

    @Test
    fun onlyNbFollowersGetsUpdated(){
        runBlocking {
            user.profile.nbFollowers = newNbFollowers

        }
        assertEquals(testBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(newNbFollowers, user.profile.nbFollowers)
        assertEquals(testNbFollowings, user.profile.nbFollowings)
        assertEquals(testProfilePicture, user.profile.profilePictureId)
    }

    @Test
    fun onlyNbFollowingsGetsUpdated(){
        runBlocking {
            user.profile.nbFollowings = newNbFollowings

        }
        assertEquals(testBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(testNbFollowers, user.profile.nbFollowers)
        assertEquals(newNbFollowings, user.profile.nbFollowings)
        assertEquals(testProfilePicture, user.profile.profilePictureId)
    }

    @Test
    fun multipleDataGetsUpdated(){
        runBlocking {
            user.profile.bio = newBio
            user.profile.nbFollowers = newNbFollowers
            user.profile.nbFollowings = newNbFollowings
            user.profile.profilePictureId = newProfilePicture

        }
        assertEquals(newBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(newNbFollowers, user.profile.nbFollowers)
        assertEquals(newNbFollowings, user.profile.nbFollowings)
        assertEquals(newProfilePicture, user.profile.profilePictureId)
    }

    /*
    @Test
    fun testUserDocument() {
        val documentReference = viewModel.userDocument("testUser")
        assertEquals(documentReference.path, "user/user-T/users/testUser")
    }

     */

    /**
     * If a user changes their username, the path will change as well.
     * The ProfileViewModel will have to be called with the new username as a parameter
     */
    @Test
    fun onlyUsernameChanges(){

        val newViewModel = ProfileViewModel(newUsername)
        runBlocking {
            user = User(username = newUsername, profile = Profile(newUsername, 2, "New bio", 20, 15))
            withContext(Dispatchers.IO) {
                FireStoreDatabaseAPI().addUserInDatabase(user).get()
            }
        }
        newViewModel.setupListener()

        assertEquals(newUsername, user.profile.username)
        assertEquals("New bio", user.profile.bio)
        assertEquals(20, user.profile.nbFollowers)
        assertEquals(15, user.profile.nbFollowings)
        assertEquals(2, user.profile.profilePictureId)
    }



}