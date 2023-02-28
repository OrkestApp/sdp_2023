package com.github.emilehreich.bootcamp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.DisplayNameSources.EMAIL
import android.view.View
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // getButton Clicked
    fun get(view: View) {
        // get the email address from the mail field
        val mail = findViewById<TextView>(R.id.mailBox)
        val phone = findViewById<TextView>(R.id.phoneBox)

        val db: DatabaseReference = Firebase.database.reference
//        db.child(PHONE_NB).setValue(EMAIL)
    }

    // set phone and email
    fun set(view: View) {
        // get the email address from the mail field
        val mail = findViewById<TextView>(R.id.mailBox)
        val phone = findViewById<TextView>(R.id.phoneBox)
        // todo
    }

}