package com.github.orkest.View

import androidx.annotation.StringRes
import com.github.orkest.R

sealed class NavigationBarClasses(val route: String, @StringRes val resourceId: Int, val iconId : Int) {
    object HomePage : NavigationBarClasses("HomePage", R.string.HomePage, R.drawable.icons8_audio_wave2_100)
    object SearchPage : NavigationBarClasses("SearchPage", R.string.SearchPage,R.drawable.icons8_search)
    object PlaylistPage : NavigationBarClasses("PlaylistPage",R.string.PlaylistPage,R.drawable.icons8_music_library_100)
    object ProfilePage : NavigationBarClasses("ProfilePage",R.string.ProfilePage,R.drawable.icons8_test_account_100)
    companion object {val  listOfNavigationItems = listOf(HomePage, SearchPage,PlaylistPage,ProfilePage)}
}
