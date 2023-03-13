package com.github.orkest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.orkest.databinding.ActivityMainBinding
import com.github.orkest.View.auth.SignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var btnSignOut: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_feed, R.id.navigation_search, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //my xml button for testing the sign out function
        auth = FirebaseAuth.getInstance()

        btnSignOut = findViewById(R.id.button_sign_out)
        btnSignOut.setOnClickListener {
            signOut()
        }
        //

    }

    /**
     * Signs out the user from the app and Google while caching the user's data
     */
    private fun signOut() {
        val intent = Intent(this, SignIn::class.java)
        auth.signOut()
        //uncomment if un-caching is needed
        //GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
        startActivity(intent)
        finish()
    }

    /* for later use
    @Composable
    fun SignOutButton() {
        Button(
            onClick = { signOut() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow))
        {
            Text("Sign Out")
        }
    }*/
}