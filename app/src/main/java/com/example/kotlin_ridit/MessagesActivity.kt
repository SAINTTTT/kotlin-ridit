package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MessagesActivity : AppCompatActivity() {

    private lateinit var dummyChatCard: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        initiComponents()
        initUI()
    }

    private fun initiComponents() {
        dummyChatCard = findViewById(R.id.dummyChatCard)
    }

    private fun initUI() {
        dummyChatCard.setOnClickListener { navigateToChatActivity() }
    }


    private fun navigateToChatActivity() {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }
}