package com.example.kotlin_ridit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunityHomeActivity : AppCompatActivity() {


    companion object {
        const val EXTRA_COMMUNITY_NAME = "communityName"
    }

    private lateinit var btnJoinCommunity: Button
    private lateinit var tvCommunityName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_home)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        btnJoinCommunity = findViewById(R.id.btnJoinCommunity)
        tvCommunityName = findViewById(R.id.tvCommunityName)
    }

    private fun initUI() {
        btnJoinCommunity.setOnClickListener { joinCommunity() }
        tvCommunityName.text = intent.getStringExtra(EXTRA_COMMUNITY_NAME)
    }

    private fun joinCommunity() {
        val db = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser?.email.toString()
        db.collection("communities")
            .whereEqualTo("name", intent.getStringExtra(EXTRA_COMMUNITY_NAME))
            .get().addOnSuccessListener { documents ->
                for (doc in documents) {
                    doc.reference.update("members", FieldValue.arrayUnion(userId))
                    Log.d("UPDATING", "$doc.id")
                }
            }.addOnFailureListener { exception ->
                Log.w("FIRESTORE-COM", "Error getting documents: ", exception)
            }


        val userRef = db.collection("users").document(userId)
        userRef.update(
            "memberOf",
            FieldValue.arrayUnion(intent.getStringExtra(EXTRA_COMMUNITY_NAME))
        )

        Toast.makeText(
            baseContext, "Te has unido a la comunidad",
            Toast.LENGTH_SHORT
        ).show()
    }
}