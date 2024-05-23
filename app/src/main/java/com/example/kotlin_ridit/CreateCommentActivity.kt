package com.example.kotlin_ridit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateCommentActivity : AppCompatActivity() {

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
        tvCommentToTitle.text = "Un post cualquiera"
        btCommentCreate.setOnClickListener {
            val comment = etCommentContent.text.toString()

            if (comment != "") {
                Log.i("COMMENT", "Comentario: $comment")
            }
        }
    }
}