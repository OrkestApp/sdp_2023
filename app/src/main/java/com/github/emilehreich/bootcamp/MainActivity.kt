package com.github.emilehreich.bootcamp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile

import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.emilehreich.bootcamp.MockDatabse.MockDatabase
import com.github.zermelo101.kotlinapp.R
import java.util.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState


class MainActivity : AppCompatActivity() {
    val mock = MockDatabase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContent {
           CreateGreetingMenu()
       }
    }

    data class BottomNavItem(
        val name: String,
        val route: String,

    )
    val bottomNavItems = listOf(
        BottomNavItem(
            name = "Home",
            route = "home",

        ),
        BottomNavItem(
            name = "Search",
            route = "Search",

        ),
        BottomNavItem(
            name = "Profile",
            route = "settings",

        ),
    )

    @Composable
    fun SearchScreen(text :String){
        androidx.compose.material3.Text(text = text)
    }




    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateGreetingMenu(){
        var text by remember { mutableStateOf("") }
        var list = fetchDatabase(text)
        val navController = rememberNavController()
        val nav =NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                SearchScreen("HOME")
            }
            composable("Search") {
                print("HELLOOOOOOOOOOOOO")
                SearchScreen("Search")
            }
            // add more destinations as necessary
        }

        val backStackEntry = navController.currentBackStackEntryAsState()

        Scaffold(
            bottomBar = {
                androidx.compose.material3.NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = item.route == backStackEntry.value?.destination?.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigate(item.route) },
                            label = {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            },
                            icon = {}
                        )
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),

                    ) {

                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it

                        }


                    )


                    LazyColumn {
                        items(list) { userName ->
                            createUser(name = userName)
                        }
                    }
                }
            }
        )





            }



    private fun CreateGreetingActivity(name :String){
        val intent = Intent(this,GreetingActivity::class.java)
        intent.putExtra("name",name)
        startActivity(intent)
    }


    @Composable
    private fun createUser(name :String){
        val profile = Random().nextInt()
        val profileString =  if( profile %2 == 0 )  R.drawable.powerrangerblue else R.drawable.powerrangerred
        Row(modifier = Modifier
            .padding(all = 8.dp)
            .clickable { CreateGreetingActivity(name) }
            .fillMaxSize()) {
            Image(
                painter = painterResource(profileString),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
            )

            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = name)

            }
        }

    }

    private fun fetchDatabase(name: String) : List<String>{
        if (name == ""){
            return listOf()
        }
        return mock.getPrefix(name)
    }
    @Composable
    private fun  CreateUi(string :List<String>){
        Column() {
            for (item in string){
                androidx.compose.material3.Text(text = item)
            }
        }
    }
}