package com.github.orkest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.data.Profile
import com.github.orkest.data.User
import com.github.orkest.ui.search.SearchUserView
import com.github.orkest.ui.search.SearchViewModel
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.*

class SearchViewModelTest {
    val user1 = User(username = "Alico", profile = Profile("Alico"))
    val user2= User(username = "Arthur", profile = Profile("Arthur"))
    val user3= User(username = "bobby", profile = Profile("bobby"))

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



        val dbAPI = FireStoreDatabaseAPI()
        val user1 = User(username = "Alico", profile = Profile("Alico"))
        val user2= User(username = "Arthur", profile = Profile("Arthur"))
        val user3= User(username = "bobby", profile = Profile("bobby"))
        dbAPI.addUserInDatabase(user1).get()
        dbAPI.addUserInDatabase(user2).get()
        dbAPI.addUserInDatabase(user3).get()



    }}
    
    @Before
    fun setup2(){
        composeTestRule.setContent {
            SearchUserView.SearchUi(viewModel = viewModel)
        }
    }


    @Test
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun whenMockUserUsernameIsTypedProfilePicIsDisplayed() = runTest{


        val usernameToType = "Alico"
        composeTestRule.onNodeWithText("Search a user here").performTextReplacement(usernameToType)
        composeTestRule.waitUntil{
            composeTestRule.onAllNodesWithText("Alico").fetchSemanticsNodes().size ==2
        }

        composeTestRule.onNodeWithContentDescription("Contact profile picture").assertIsDisplayed()

    }


    @Test
    fun whenFirstLettersOfUsernamesIsTypedDisplayCorrectUsers(){



        var usernameToType = "Al"
        composeTestRule.onNodeWithText("Search a user here").performTextReplacement(usernameToType)
        composeTestRule.waitUntil{
            composeTestRule.onAllNodesWithText("Alico").fetchSemanticsNodes().size ==1
        } //Timeout after 1s
        composeTestRule.onNodeWithText("Alico").assertIsDisplayed()

        usernameToType = "bo"
        composeTestRule.onNodeWithText("Al").performTextReplacement(usernameToType)
        composeTestRule.waitUntil{
            composeTestRule.onAllNodesWithText("bobby").fetchSemanticsNodes().size ==1
        } //Timeout after 1 s
        composeTestRule.onNodeWithText("bobby").assertIsDisplayed()


    }





    @Test
    fun theSearchBarCanBeTypedIn(){

        val textToType = "dummyString"

        composeTestRule.onNodeWithText("Search a user here").performTextReplacement(textToType)

        composeTestRule.onNodeWithText(textToType).assertIsDisplayed()


    }





    @Test
    fun theViewModelFetchOnlyRetrieveUserThatBeginWithTheArgument(){


            val it  = viewModel.searchUserInDatabase("A").get()
            MatcherAssert.assertThat(mutableListOf(user1,user2), `is`(it))

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

