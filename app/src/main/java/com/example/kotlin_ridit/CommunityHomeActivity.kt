package com.example.kotlin_ridit

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CommunityHomeActivity : AppCompatActivity() {

    private lateinit var btnJoinCommunity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_home)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        btnJoinCommunity = findViewById(R.id.btnJoinCommunity)
    }

    private fun initUI() {
        btnJoinCommunity.setOnClickListener { joinCommunity() }
    }

    private fun joinCommunity() {
        // TODO unirse a la comunidad
        Toast.makeText(
            baseContext, "Te has unido a la comunidad",
            Toast.LENGTH_SHORT
        ).show()
    }
}