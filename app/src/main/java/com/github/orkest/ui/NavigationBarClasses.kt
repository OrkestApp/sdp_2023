package com.github.orkest.ui

import androidx.annotation.StringRes
import com.github.orkest.R
import com.github.orkest.R.*

/**
 * The goal of this class is to encapsulate all the elements of the bottom navigation bar
 * Each object represent an element of the bottom nav bar, defined by the route (used to know what composable to launch when clicked)
 * the ressource Id (the description), and the IconId(used to retrieve the icon for the given element of the nav bar)
 */
sealed class NavigationBarClasses(val route: String, @StringRes val resourceId: Int, val iconId : Int) {
    object HomePage : NavigationBarClasses("HomePage", string.HomePage, drawable.icons8_audio_wave2_100)
    object SearchPage : NavigationBarClasses("SearchPage", string.SearchPage, drawable.icons8_search)
    object CreatePostPage : NavigationBarClasses("Shazam", string.ShazamPage, drawable.shazam)
    object PlaylistPage : NavigationBarClasses("PlaylistPage", string.PlaylistPage, drawable.icons8_music_library_100)
    object ProfilePage : NavigationBarClasses("ProfilePage", string.ProfilePage, drawable.icons8_test_account_100)
    companion object {val  listOfNavigationItems = listOf(
        HomePage, SearchPage,
        PlaylistPage,
        ProfilePage
    )}
}
