package com.example.kotlin_ridit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PublicProfileActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_NAME = "pepino"
    }

    private lateinit var tvUsernamePublicProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_profile)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        tvUsernamePublicProfile = findViewById(R.id.tvUsernamePublicProfile)
    }

    private fun initUI() {
        tvUsernamePublicProfile.text = intent.getStringExtra(EXTRA_USER_NAME)
    }
}