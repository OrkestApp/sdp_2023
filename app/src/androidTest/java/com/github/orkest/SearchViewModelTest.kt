package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.View.search.SearchUserView
import com.github.orkest.ViewModel.search.SearchViewModel
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.*
import java.util.concurrent.CompletableFuture

class SearchViewModelTest {

    @get:Rule
    var composeTestRule =  createComposeRule()



    companion object{
        private lateinit var viewModel : SearchViewModel

    @BeforeClass
    @JvmStatic
     fun setup(){
        val woman1 = hashMapOf(
            "username" to "Alico"
        )
        val man1 = hashMapOf(
            "username" to "Arthur"
        )
        val man2 = hashMapOf(
            "username" to "bobby"
        )
        viewModel = SearchViewModel()
        viewModel.db.useEmulator("10.0.2.2", 8080)
        viewModel.db.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
        val future1 = CompletableFuture<Any>()
        val future2 = CompletableFuture<Any>()
        val future3 = CompletableFuture<Any>()
        viewModel.db.collection("user/user-A/users").document("Alico").set(woman1).addOnSuccessListener { future1.complete(1) }
        viewModel.db.collection("user/user-A/users").document("Arthur").set(man1).addOnSuccessListener { future2.complete(2) }
        viewModel.db.collection("user/user-B/users").document("bobby").set(man2).addOnSuccessListener { future3.complete(3) }

        future1.get()
        future2.get()
        future3.get()


    }}
    
    @Before
    fun setup2(){
        composeTestRule.setContent {
            SearchUserView.SearchUi(viewModel = viewModel)
        }
    }


    @Test
    fun whenMockUserUsernameIsTypedProfilePicIsDisplayed() = runTest{


        val usernameToType = "Alico"
        composeTestRule.onNodeWithText("").performTextReplacement(usernameToType)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Contact profile picture").assertIsDisplayed()
    }

    @Test
    fun whenFirstLettersOfUsernamesIsTypedDisplayCorrectUsers(){

        var usernameToType = "o"
        composeTestRule.onNodeWithText("").performTextReplacement(usernameToType) //fake fetch

         usernameToType = "A"
        composeTestRule.onNodeWithText("o").performTextReplacement(usernameToType)

        composeTestRule.onNodeWithText("Alico").assertIsDisplayed()
        composeTestRule.onNodeWithText("bobby").assertDoesNotExist()

        usernameToType = "b"
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("A").performTextReplacement(usernameToType)
        composeTestRule.onNodeWithText("bobby").assertIsDisplayed()


    }





    @Test
    fun theSearchBarCanBeTypedIn(){

        val textToType = "dummyString"

        composeTestRule.onNodeWithText("").performTextInput(textToType)

        composeTestRule.onNodeWithText(textToType).assertIsDisplayed()


    }





    @Test
    fun theViewModelFetchOnlyRetrieveUserThatBeginWithTheArgument(){


            val it  = viewModel.searchUserInDatabase("A").get()
            MatcherAssert.assertThat(mutableListOf("Alico","Arthur"), `is`(it))

            val it2 = viewModel.searchUserInDatabase("Z").get()
            MatcherAssert.assertThat(mutableListOf(),`is` (it2))


    }



    @Test
    fun searchViewModelReturnEmptyListWithNullInput() {

        viewModel.searchUserInDatabase("").thenAccept {
            MatcherAssert.assertThat(mutableListOf(), `is`(it))
        }
    }
}

