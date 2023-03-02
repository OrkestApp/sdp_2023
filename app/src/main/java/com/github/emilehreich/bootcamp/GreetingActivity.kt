package com.github.emilehreich.bootcamp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GreetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        val greet = findViewById<TextView>(R.id.greetingName)
        val name = intent.getStringExtra("name")

        greet.text = "Hello $name"


        // instantiating Fragments
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()

        val homeButton = findViewById<Button>(R.id.btnHomeFragment)
        val profileButton = findViewById<Button>(R.id.btnProfileFragment)

        homeButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flMainFragments, homeFragment)
                commit()
            }
        }

        profileButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flMainFragments, profileFragment)
                commit()
            }
        }
    }
}