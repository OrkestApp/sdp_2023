package com.github.orkest.View

import androidx.annotation.StringRes
import com.github.orkest.R

/**
 * The goal of this class is to encapsulate all the elements of the bottom navigation bar
 * Each object represent an element of the bottom nav bar, defined by the route (used to know what composable to launch when clicked)
 * the ressource Id (the description), and the IconId(used to retrieve the icon for the given element of the nav bar)
 */
sealed class NavigationBarClasses(val route: String, @StringRes val resourceId: Int, val iconId : Int) {
    object HomePage : NavigationBarClasses("HomePage", R.string.HomePage, R.drawable.icons8_audio_wave2_100)
    object SearchPage : NavigationBarClasses("SearchPage", R.string.SearchPage,R.drawable.icons8_search)
    object PlaylistPage : NavigationBarClasses("PlaylistPage",R.string.PlaylistPage,R.drawable.icons8_music_library_100)
    object ProfilePage : NavigationBarClasses("ProfilePage",R.string.ProfilePage,R.drawable.icons8_test_account_100)
    companion object {val  listOfNavigationItems = listOf(HomePage, SearchPage,PlaylistPage,ProfilePage)}
}
