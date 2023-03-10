package com.github.orkest.View.auth

import android.annotation.SuppressLint
import android.widget.RadioGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.github.orkest.Model.Providers
import com.github.orkest.View.theme.Teal200
import com.github.orkest.View.theme.White


// Needs to be initialized in the main activity and called as a callback function
// after the google authentication sign up returns
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpForm(navController: NavController, viewModel: AuthViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Your Profile") },
            )},
        content = {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val expanded = remember { mutableStateOf(false) }


                Text(
                    text = "Create Your Profile",
                    style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    label = { Text(text = "Username") },
                    value = viewModel.getUsername(),
                    onValueChange = { viewModel.updateUsername(it) }
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    modifier = Modifier.width(280.dp),
                    label = { Text(text = "Profile Description") },
                    value = viewModel.getBio(),
                    onValueChange = { viewModel.updateBio(it) },
                )

                Spacer(modifier = Modifier.height(15.dp))

                Box {


                    Button(
                        colors= ButtonDefaults.buttonColors(backgroundColor = Color(217,217,217)),
                        onClick = {expanded.value = true},
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .width(280.dp)
                            .height(50.dp)
                            .background(Color.Gray)
                    ) {
                        Text(
                            text =  "Service Provider: ${viewModel.getProvider().value}",
                            style = TextStyle(fontSize = 15.sp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier
                            .background(White)
                            .width(280.dp)
                    ) {
                        Providers.values().forEachIndexed { _, option ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.updateProvider(option)
                                    expanded.value = false
                                }
                            ) {
                                Text(text = option.value)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))


                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Create Profile",
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
            }

        })
}






