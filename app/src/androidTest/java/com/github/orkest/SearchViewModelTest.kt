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
        viewModel = SearchViewModel(true)
        viewModel.db.useEmulator("10.0.2.2", 8080)
        viewModel.db.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
        viewModel.db.collection("user/user-A/users").document("users").set(woman1)
        
    }}
    
    @Before
    fun setup2(){
        composeTestRule.setContent {
            SearchUserView.SearchUi(viewModel = viewModel)
        }
    }

    @After
    fun final(){

    }
    @Test
    fun whenMockUserUsernameIsTypedIsDisplayed() = runTest{


        val usernameToType = "Alico"
        composeTestRule.onNodeWithText("").performTextReplacement(usernameToType)
        composeTestRule.onNodeWithContentDescription("Contact profile picture").assertIsDisplayed()
    }
    @Test
    fun addition_isCorrect() {

        Assert.assertEquals(4, 2 + 2)


    }






    @Test
    fun theSearchBarCanBeTypedIn(){


        val textToType = "DummyString"

        composeTestRule.onNodeWithText("").performTextInput(textToType)

        composeTestRule.onNodeWithText(textToType).assertIsDisplayed()


    }







    @Test
    fun theViewModelFetchOnlyRetrieveUserThatBeginWithTheArgument(){



            viewModel.searchUserInDatabase("bo").thenAccept {
                MatcherAssert.assertThat(mutableListOf("bob", "bobby"), `is`(it))
            }





    }



    @Test
    fun searchViewModelReturnEmptyListWithNullInput() {

        viewModel.searchUserInDatabase("").thenAccept {
            MatcherAssert.assertThat(mutableListOf(), `is`(it))
        }
    }
}

