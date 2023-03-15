package com.github.orkest.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class mockDatabaseSearch {

    companion object {
        fun returnMockDataBase(): FirebaseFirestore {
            val firestore = Firebase.firestore
            val woman1 = hashMapOf(
                "username" to "Alico"
            )
            val man1 = hashMapOf(
                "username" to "bob"
            )
            val man2 = hashMapOf(
                "username" to "bobby"
            )


            firestore.useEmulator("10.0.2.2", 8080)
            firestore.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }
            firestore.collection("users").document("userA").set(woman1)


            /* Uncomment to add fake users in the mock database

        firestore.collection("users").document("userB").set(man1)
        firestore.collection("users").document("userC").set(man2)
        *
         */

            return firestore
        }
    }



}