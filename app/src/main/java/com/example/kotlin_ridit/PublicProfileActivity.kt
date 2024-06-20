package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_ridit.chat.ChatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PublicProfileActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_NAME = "pepino"
    }

    private lateinit var tvUsernamePublicProfile: TextView
    private lateinit var btnMessage: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_profile)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        tvUsernamePublicProfile = findViewById(R.id.tvUsernamePublicProfile)
        btnMessage = findViewById(R.id.btnMessage)
    }

    private fun initUI() {
        tvUsernamePublicProfile.text = intent.getStringExtra(EXTRA_USER_NAME)
        btnMessage.setOnClickListener{chatWithUser()}
    }

    private fun chatWithUser() {
        val db = Firebase.firestore
        db.collection("users")
            .whereEqualTo(FieldPath.documentId(), intent.getStringExtra(EXTRA_USER_NAME)).get()
            .addOnSuccessListener { snapshot ->
                for (user in snapshot) {
                    if(user.data["uid"]!=null) {
                        Log.d("CHAT", "user.data => ${user.data}")
                        val intentToChat = Intent(this, ChatActivity::class.java)
                        intentToChat.putExtra("uid", user.data["uid"] as String)
                        startActivity(intentToChat)
                    }
                }
             }
    }
}