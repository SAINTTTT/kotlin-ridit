package com.example.kotlin_ridit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateCommunityActivity : AppCompatActivity() {
    private lateinit var etCommunityName: EditText
    private lateinit var etCommunityDescription: EditText
    private lateinit var btnCreateCommunity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        etCommunityName = findViewById(R.id.etCommunityName)
        etCommunityDescription = findViewById(R.id.etCommunityDescription)
        btnCreateCommunity = findViewById(R.id.btnCreateCommunity)
    }

    private fun initUI() {
        btnCreateCommunity.setOnClickListener { createCommunity() }
    }

    private fun createCommunity() {
        val communityName = etCommunityName.text.toString()
        val communityDescription = etCommunityDescription.text.toString()

        if (communityName.isNotEmpty() && communityDescription.isNotEmpty()) {
            //TODO hay que validar que ese nombre no exista
            Toast.makeText(
                baseContext, "Comunidad Creada",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}