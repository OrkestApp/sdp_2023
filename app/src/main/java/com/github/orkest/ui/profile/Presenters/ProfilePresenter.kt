package com.github.orkest.ui.profile.Presenters

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.github.orkest.ui.profile.Interfaces.ProfileContract
import com.github.orkest.ui.profile.Models.ProfileModel

/**
 * Acts as mediator between the Model and View classes.
 * Handles business logic
 * Retrieves data from the Model and display it on the View class
 */

class ProfilePresenter(private val view: ProfileContract.View) : ProfileContract.Presenter{

    private val model: ProfileContract.Model = ProfileModel()

    override fun fetchProfileData() {
        model.getProfileData { profile ->
            val s = view.displayProfileData(profile)
        }
    }


}