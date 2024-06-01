package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var tvPersonalData: TextView
    private lateinit var tvPostHistory: TextView
    private lateinit var tvCommunities: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        tvPersonalData = findViewById(R.id.tvPersonalData)
        tvPostHistory = findViewById(R.id.tvPostHistory)
        tvCommunities = findViewById(R.id.tvCommunities)
    }

    private fun initUI() {
        tvPersonalData.setOnClickListener { navigateToPersonalData() }
        tvPostHistory.setOnClickListener{ navigateToPostHistory() }
        tvCommunities.setOnClickListener{ navigateToCommunities() }
    }

    private fun navigateToPersonalData() {
        val intent = Intent(this, PersonalDataActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPostHistory() {
        val intent = Intent(this, PostHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToCommunities() {
        val intent = Intent(this, CommunitiesActivity::class.java)
        startActivity(intent)
    }

}