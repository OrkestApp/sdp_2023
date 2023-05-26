package com.github.orkest.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.room.Room
import com.github.orkest.data.Constants

import com.github.orkest.View.feed.FeedActivity
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.domain.persistence.AppDatabase
import com.github.orkest.shazam.ui.ShazamSong
import com.github.orkest.ui.Camera.CameraView
import com.github.orkest.ui.profile.ProfileActivity
import com.github.orkest.ui.profile.ProfileActivityScreen
import com.github.orkest.ui.search.SearchUserView
import com.github.orkest.ui.sharing.UsersList
import com.github.orkest.ui.feed.PostViewModel
import com.github.orkest.ui.profile.ProfileViewModel
import com.github.orkest.ui.search.SearchViewModel


class NavigationBar {


    companion object {
        private val viewModel = SearchViewModel()
        @SuppressLint("UnrememberedMutableState")
        @Composable
        fun CreateNavigationBar(navController: NavHostController, currentUser: String, activity: MainActivity) {

            val context = LocalContext.current
            val postsDatabase: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "posts-db")
                .build()
            val sharedPlaylistDB: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "sharedPlaylist-db")
                .build()
            Scaffold(
                bottomBar = {
                    BottomNavigation(backgroundColor = Color.White) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        NavigationBarClasses.listOfNavigationItems.forEach { item ->
                            BottomNavigationItem(
                                icon = {
                                    Image(
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
                    composable("HomePage") { FeedActivity(postsDatabase, context, PostViewModel()) }
                    composable("SearchPage") { SearchUserView.SearchUi(viewModel = viewModel) }
                    composable("ShazamPage") {
                        if(FireStoreDatabaseAPI.isOnline(context))
                        {
                            ShazamSong(activity)
                            val intent = Intent(context, CameraView::class.java)
                            context.startActivity(intent)
                        }
                        else{
                            Toast.makeText(context, "No internet connection. Unable to post and shazam.", Toast.LENGTH_LONG).show()
                        }

                    }
                    composable("PlaylistPage") {
                        UsersList(sharedPlaylistDB, context)
                    }
                    composable("ProfilePage") {
                            ProfileActivityScreen(ProfileActivity(), viewModel = ProfileViewModel(
                                Constants.CURRENT_LOGGED_USER))
                    }
                }
            }
        }
    }
}