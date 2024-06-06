package com.example.kotlin_ridit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateCommunityActivity : AppCompatActivity() {
    private lateinit var etCommunityName: EditText
    private lateinit var etCommunityDescription: EditText
    private lateinit var btnCreateCommunity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        etCommunityName = findViewById(R.id.etCommunityName)
        etCommunityDescription = findViewById(R.id.etCommunityDescription)
        btnCreateCommunity = findViewById(R.id.btnCreateCommunity)
    }

    private fun initUI() {
        btnCreateCommunity.setOnClickListener { createCommunity() }
    }

    private fun createCommunity() {
        val communityName = etCommunityName.text.toString()
        val communityDescription = etCommunityDescription.text.toString()
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        if (communityName.isNotEmpty() && communityDescription.isNotEmpty()) {
            //TODO hay que validar que ese nombre no exista

            db.collection("communities").document()
                .set(
                    hashMapOf(
                        "creator" to currentUser?.email,
                        "description" to communityDescription,
                        "name" to communityName,
                        "members" to currentUser?.email, // cambiar esto
                        "posts" to null, // cambiar esto
                        "creationDate" to java.util.Date()
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(
                        baseContext, "Comunidad Creada",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d(
                        "FIRESTORE-CREATE-COMMUNITY",
                        "DocumentSnapshot successfully written!"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        "FIRESTORE-CREATE-COMMUNITY",
                        "Error writing document",
                        e
                    )
                }
            finish()
        }
    }
}