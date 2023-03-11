package com.github.orkest.ui.profile.Interfaces

import androidx.compose.runtime.Composable
import com.github.orkest.DataModel.Profile
import com.github.orkest.DataModel.User


/**
 * Define the contract between the different layers of the MVP pattern
 * Good practice, easier to manage and maintain the code, not mandatory
 */
interface ProfileContract {


    interface View {
        fun displayProfileData(profile: Profile): Profile
    }

    interface Presenter {
        fun fetchProfileData()
    }

    interface Model {
        fun getProfileData(callback: (Profile) -> Unit)
    }

}