package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_ridit.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap

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
        btnMessage.setOnClickListener { chatWithUser() }
    }

    private fun chatWithUser() {
        val db = Firebase.firestore
        db.collection("users")
            .whereEqualTo(FieldPath.documentId(), intent.getStringExtra(EXTRA_USER_NAME)).get()
            .addOnSuccessListener { snapshot ->
                for (user in snapshot) {
                    if (user.data["uid"] != null) {
                        // si es la primera vez que chatean guardarlo en la BBDD
                        addChatHistory(user.data["username"].toString(), user.data["uid"].toString())
                        Log.d("CHAT", "user.data => ${user.data}")
                        val intentToChat = Intent(this, ChatActivity::class.java)
                        intentToChat.putExtra("uid", user.data["uid"] as String)
                        startActivity(intentToChat)
                    }
                }
            }
    }

    private fun addChatHistory(username: String, uid: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        // mi historial
        db.collection("users").whereEqualTo(FieldPath.documentId(), currentUser?.email)
            .get().addOnSuccessListener { snapshot ->
                for(document in snapshot) {
                    document.reference.update("chatsWith", FieldValue.arrayUnion(hashMapOf("name" to username, "uid" to uid)))
                }
            }
       // su historial
        db.collection("users").whereEqualTo(FieldPath.documentId(), intent.getStringExtra(EXTRA_USER_NAME))
            .get().addOnSuccessListener { snapshot ->
                for(document in snapshot) {
                    document.reference.update("chatsWith", FieldValue.arrayUnion(hashMapOf("name" to currentUser?.email, "uid" to currentUser?.uid)))
                }
            }

    }
}