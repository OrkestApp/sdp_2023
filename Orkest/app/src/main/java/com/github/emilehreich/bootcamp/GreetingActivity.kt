package com.github.emilehreich.bootcamp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GreetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        val greet = findViewById<TextView>(R.id.greetingName)
        greet.text = intent.getStringExtra("name")
    }
}