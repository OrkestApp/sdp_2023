package com.github.emilehreich.bootcamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContent {
           CreateGreetingMenu()
       }
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateGreetingMenu(){
        var text by remember { mutableStateOf("Enter Here") }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Column (verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)){

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Username") }
                )

                Button(onClick = { CreateGreetingActivity(text) }) {
                    Text(text = "Next")
                }


            }
        }
    }


    private fun CreateGreetingActivity(name :String){
        val intent = Intent(this,GreetingActivity::class.java)
        intent.putExtra("name",name)
        startActivity(intent)
    }
}