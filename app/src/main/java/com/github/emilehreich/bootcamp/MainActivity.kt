package com.github.emilehreich.bootcamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var btnSignOut: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        btnSignOut = findViewById(R.id.btn_sign_out)
        btnSignOut.setOnClickListener {
            signOut()
        }
    }

    fun greetName(view: View) {
        val intent = Intent(this, GreetingActivity::class.java)
        val name = findViewById<TextView>(R.id.mainText)
        intent.putExtra("name",name.text.toString())
        startActivity(intent)
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(this@MainActivity, SignIn::class.java)
        FirebaseAuth.getInstance().signOut()
        GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess()
        startActivity(intent)
        finish()
    }
}