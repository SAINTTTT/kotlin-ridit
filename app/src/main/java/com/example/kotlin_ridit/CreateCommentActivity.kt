package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateCommentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COMMENT_POST_ID = "postId"
        const val EXTRA_COMMENT_POST_TITLE = "postTitle"
    }

    private lateinit var tvCommentToTitle: TextView
    private lateinit var etCommentContent: EditText
    private lateinit var btCommentCreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        tvCommentToTitle = findViewById(R.id.tvCommentToTitle)
        etCommentContent = findViewById(R.id.etCommentContent)
        btCommentCreate = findViewById(R.id.btCommentCreate)
    }

    private fun initUI() {
        // provisorio
        tvCommentToTitle.text = intent.getStringExtra(EXTRA_COMMENT_POST_TITLE)
        btCommentCreate.setOnClickListener {
            val comment = etCommentContent.text.toString()

            if (comment != "") {
                val currentUser = Firebase.auth.currentUser
                val db = Firebase.firestore
                db.collection("comments").document()
                    .set(
                        hashMapOf(
                            "commentsTo" to intent.getStringExtra(EXTRA_COMMENT_POST_TITLE),
                            "content" to comment,
                            "creator" to currentUser?.email,
                            "creationDate" to java.util.Date()
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.comment_created), Toast.LENGTH_SHORT
                        ).show();
                        etCommentContent.text.clear()

                        Log.d(
                            "FIRESTORE-CREATE-COMMENT",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            "FIRESTORE-CREATE-COMMENT",
                            "Error writing document",
                            e
                        )
                    }
                Log.i("POST", "comentario: $comment")
            }
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}

