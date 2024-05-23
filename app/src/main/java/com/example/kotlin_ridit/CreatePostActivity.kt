package com.example.kotlin_ridit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
                Log.i("POST", "$title, $content")
            }
        }
    }
}