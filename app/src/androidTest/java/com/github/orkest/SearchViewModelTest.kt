package com.github.orkest

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.github.orkest.ViewModel.search.SearchViewModel
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val composeTestRule =  createComposeRule()
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }

    @Test
    fun searchViewModelReturnEmptyListWithNullInput(){
        var listToBeModified = mutableListOf("DummyValue")
        val dummyLambda : (MutableList<String>)-> Unit = { listToBeModified = it }
        SearchViewModel().searchUserInDatabase("",dummyLambda)
        assertThat(mutableListOf(), `is`(listToBeModified))
    }


    @Test
    fun theSearchBarIsActuallyDisplayed(){

    }
}

