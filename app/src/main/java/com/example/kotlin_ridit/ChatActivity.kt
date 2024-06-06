package com.example.kotlin_ridit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChatActivity : AppCompatActivity() {
    private lateinit var etChatInputText: EditText
    private lateinit var btnSendMessage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        etChatInputText = findViewById(R.id.etChatInputText)
        btnSendMessage = findViewById(R.id.btnSendMessage)
    }

    private fun initUI() {
        btnSendMessage.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val message = etChatInputText.text.toString()
        if (message.isNotEmpty()) {
            Toast.makeText(
                baseContext, "Mensaje enviado",
                Toast.LENGTH_SHORT
            ).show()
            etChatInputText.text.clear()
        }
    }


}