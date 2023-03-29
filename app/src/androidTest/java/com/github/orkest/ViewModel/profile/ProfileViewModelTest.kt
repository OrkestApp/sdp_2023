package com.github.orkest.ViewModel.profile

import com.github.orkest.Constants
import com.github.orkest.Model.Profile
import com.github.orkest.Model.User
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

// run firebase emulators:start --only firestore in terminal before
class ProfileViewModelTest {

    private var testUserName = "testUser"
    private var testBio = "This is my bio"
    private var testNbFollowers = 10
    private var testNbFollowings = 34
    private var testProfilePicture = 45

    private val newUsername = "newUsername"
    private val newBio = "My new bio"
    private val newNbFollowers = 32
    private val newNbFollowings = 65
    private val newProfilePicture = 928

    private lateinit var user: User
    private lateinit var newUser: User
    private var viewModel: ProfileViewModel = ProfileViewModel(testUserName)
    private val newViewModel: ProfileViewModel = ProfileViewModel(newUsername)

    @Before
    fun setUp() {
        Constants.currentLoggedUser = testUserName
        runBlocking {
            user = User(profile = Profile(testUserName, testProfilePicture, testBio, testNbFollowers, testNbFollowings))
            viewModel.userDocument(testUserName).set(user).await()
        }
        runBlocking {
            newUser = User(profile = Profile(newUsername, newProfilePicture, newBio, newNbFollowers, newNbFollowings))
            newViewModel.userDocument(newUsername).set(newUser).await()
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
    fun multipleDataGetsUpdated(){
        runBlocking {
            user.profile.bio = newBio
            user.profile.nbFollowers = newNbFollowers
            user.profile.nbFollowings = newNbFollowings
            user.profile.profilePictureId = newProfilePicture
            viewModel.userDocument(testUserName).set(user).await()
        }
        assertEquals(newBio, user.profile.bio)
        assertEquals(testUserName, user.profile.username)
        assertEquals(newNbFollowers, user.profile.nbFollowers)
        assertEquals(newNbFollowings, user.profile.nbFollowings)
        assertEquals(newProfilePicture, user.profile.profilePictureId)
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

    @Test
    fun isUserFollowedReturnsTrueWhenFollowed(){
        runBlocking {
            user.followings.add(newUsername)
            viewModel.userDocument(testUserName).set(user).await()
            newUser.followers.add(testUserName)
            newViewModel.userDocument(newUsername).set(newUser).await()
        }

        newViewModel.isUserFollowed().whenComplete{_, _ ->
            assertEquals(true, newViewModel.isUserFollowed.value)
        }
    }

    @Test
    fun isUserFollowedReturnsFalseWhenNotFollowed(){
        newViewModel.isUserFollowed().whenComplete{_, _ ->
            assertEquals(false, newViewModel.isUserFollowed.value)
        }
    }

    @Test
    fun followFunctionalityUpdatesUsers(){
        newViewModel.follow().whenComplete {_,_ ->
            assertEquals(true, user.followings.contains(newUsername))
            assertEquals(true, newUser.followers.contains(testUserName))
            assertEquals(testNbFollowings+1, user.profile.nbFollowings)
            assertEquals(newNbFollowers+1, newUser.profile.nbFollowers)
        }
    }

    @Test
    fun unFollowFunctionalityUpdatesUsers(){
        newViewModel.unfollow().whenComplete {_,_ ->
            assertEquals(false, user.followings.contains(newUsername))
            assertEquals(false, newUser.followers.contains(testUserName))
            assertEquals(testNbFollowings-1, user.profile.nbFollowings)
            assertEquals(newNbFollowers-1, newUser.profile.nbFollowers)
        }
    }

    @Test
    fun unFollowNeverGoesUnderZero(){
        runBlocking {
            newUser.profile.nbFollowers = 0
            newViewModel.userDocument(newUsername).set(newUser).await()
            user.profile.nbFollowings = 0
            viewModel.userDocument(testUserName).set(user).await()
        }
        newViewModel.unfollow().whenComplete{_, _ ->
            assertEquals(0, newUser.profile.nbFollowers)
            assertEquals(0, user.profile.nbFollowings)
        }
    }

    @Test
    fun isUserUpdatedThrowsExceptionWhenCalledInCurrentProfile(){
        try { viewModel.isUserFollowed() } catch(e: java.lang.IllegalArgumentException){
            assertEquals("Cannot call this function when visiting the current logged-in user's profile", e.message)
        }
    }

    @Test
    fun followThrowsExceptionWhenCalledInCurrentProfile(){
        try { viewModel.follow() } catch(e: java.lang.IllegalArgumentException){
            assertEquals("Cannot call this function when visiting the current logged-in user's profile", e.message)
        }
    }

    @Test
    fun unfollowThrowsExceptionWhenCalledInCurrentProfile(){
        try { viewModel.unfollow() } catch(e: java.lang.IllegalArgumentException){
            assertEquals("Cannot call this function when visiting the current logged-in user's profile", e.message)
        }
    }
}