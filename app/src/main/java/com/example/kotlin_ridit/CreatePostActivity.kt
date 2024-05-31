package com.example.kotlin_ridit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class CreatePostActivity : AppCompatActivity() {

    private lateinit var etPostTitle: EditText
    private lateinit var etPostContent: EditText
    private lateinit var btPostCreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        etPostTitle = findViewById(R.id.etPostTitle)
        etPostContent = findViewById(R.id.etPostContent)
        btPostCreate = findViewById(R.id.btPostCreate)
    }

    private fun initUI() {
        btPostCreate.setOnClickListener {
            val title = etPostTitle.text.toString()
            val content = etPostContent.text.toString()
            if (title != "" && content != "") {
                val db = Firebase.firestore
                db.collection("posts").document()
                    .set(
                        hashMapOf(
                            "creator" to "juancito",
                            "title" to title,
                            "content" to content,
                            "upvoteCount" to 0,
                            "downvoteCount" to 0,
                            "commentsCount" to 0,
                            "creationDate" to java.util.Date()
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.post_creation_success), Toast.LENGTH_SHORT
                        ).show();
                        etPostTitle.text.clear()
                        etPostContent.text.clear()
                        Log.d(
                            "FIRESTORE-CREATE-POST",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            "FIRESTORE-CREATE-POST",
                            "Error writing document",
                            e
                        )
                    }
                Log.i("POST", "$title, $content")
            }
        }
    }
}