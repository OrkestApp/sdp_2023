package com.github.orkest.View

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.orkest.View.feed.FeedActivity
import com.github.orkest.View.profile.ProfileActivity
import com.github.orkest.View.profile.ProfileActivityScreen
import com.github.orkest.View.profile.ProfileActivitySetting
import com.github.orkest.View.profile.ProfileTopInterface
import com.github.orkest.View.search.SearchUserView
import com.github.orkest.ViewModel.profile.ProfileViewModel
import com.github.orkest.ViewModel.search.SearchViewModel


class NavigationBar {


    companion object {
        private val viewModel = SearchViewModel()
        @Composable
        fun CreateNavigationBar(navController: NavHostController, currentUser: String) {

            Scaffold(
                bottomBar = {
                    BottomNavigation(backgroundColor = Color.White) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        NavigationBarClasses.listOfNavigationItems.forEach { item ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.iconId),
                                        "",
                                        Modifier.size(25.dp)
                                    )
                                },
                                label = { Text(stringResource(item.resourceId)) },
                                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                onClick = {
                                    navController.navigate(item.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "HomePage",
                    Modifier.padding(padding)
                ) {
                    composable("HomePage") { FeedActivity() }
                    composable("SearchPage") { SearchUserView.SearchUi(viewModel = viewModel) }
                    composable("PlaylistPage") { Text(text = "Playlist tab") }
                    composable("ProfilePage") {
                            ProfileActivityScreen(ProfileActivity(), viewModel = ProfileViewModel("JohnDoe"))
                    }
                }
            }
        }
    }
}