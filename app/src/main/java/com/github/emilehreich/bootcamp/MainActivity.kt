package com.github.emilehreich.bootcamp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity() {

    companion object{
        var database = Firebase.database
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // on click getButton the email address associated with the phone number
    fun get(view: View) {
        // get the email address from the mail field
        val mail = findViewById<TextView>(R.id.mailBox)
        val phone = findViewById<TextView>(R.id.phoneBox)

        val future = CompletableFuture<String>()
        val db: DatabaseReference = Firebase.database.reference

        db.child(phone.text.toString()).get().addOnSuccessListener {
            if (it.value == null) future.completeExceptionally(NoSuchFieldException())
            else future.complete(it.value as String)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        future.thenAccept {
            mail.text = it
        }
    }

    // set phone and email
    fun set(view: View) {
        val db = database.reference
        // get the email address from the mail field
        val mail = findViewById<TextView>(R.id.mailBox)
        val phone = findViewById<TextView>(R.id.phoneBox)

        db.child(phone.text.toString()).setValue(mail.text.toString())
    }

}