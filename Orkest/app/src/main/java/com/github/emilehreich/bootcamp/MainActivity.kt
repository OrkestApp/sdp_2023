package com.github.emilehreich.bootcamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun greetName(view: View) {
        val intent = Intent(this, GreetingActivity::class.java)
        val name = findViewById<TextView>(R.id.mainText)
        intent.putExtra("name",name.text.toString())
        startActivity(intent)


    }
}