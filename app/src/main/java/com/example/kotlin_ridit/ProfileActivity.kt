package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var tvPersonalData: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        tvPersonalData = findViewById(R.id.tvPersonalData)
    }

    private fun initUI() {
        tvPersonalData.setOnClickListener { navigateToPersonalData() }
    }

    private fun navigateToPersonalData() {
        val intent = Intent(this, PersonalDataActivity::class.java)
        startActivity(intent)
    }

}