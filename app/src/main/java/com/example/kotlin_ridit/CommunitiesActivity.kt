package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CommunitiesActivity : AppCompatActivity() {

    private lateinit var btnCreateCommunity: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communities)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        btnCreateCommunity = findViewById(R.id.btnCreateCommunity)
    }

    private fun initUI() {
        btnCreateCommunity.setOnClickListener { navigateToCreateCommunity() }
    }

    private fun navigateToCreateCommunity() {
        val intent = Intent(this, CreateCommunityActivity::class.java)
        startActivity(intent)
    }
}