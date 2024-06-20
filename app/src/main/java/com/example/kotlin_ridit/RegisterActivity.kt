package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var etRegisterEmail: EditText
    private lateinit var etRegisterUsername: EditText
    private lateinit var etRegisterPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvRegisterGoToLogin: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        etRegisterUsername = findViewById(R.id.etRegisterUsername)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvRegisterGoToLogin = findViewById(R.id.tvRegisterGoToLogin)
        auth = FirebaseAuth.getInstance()
    }

    private fun initUI() {

        tvRegisterGoToLogin.setOnClickListener { finish() }

        btnRegister.setOnClickListener() {
            val username = etRegisterUsername.text.toString()
            val email = etRegisterEmail.text.toString()
            val password = etRegisterPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Una vez registrado, hago el insert en la DB
                            Firebase.firestore.collection("users").document(email)
                                .set(hashMapOf("username" to username))
                            // guardar en la base para los chats
                            addUserToChatDatabase(username, email, auth.currentUser?.uid)
                            Log.d("REGISTER", "createUserWithEmail:success")
                            val user = auth.currentUser
                            // redirige al home?
                            startActivity(Intent(this, HomeActivity::class.java))
                        } else {
                            Log.w("REGISTER", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext, "Please enter email and password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addUserToChatDatabase(username: String, email: String, uid: String?) {
        val db = FirebaseDatabase.getInstance().getReference()
        if (uid != null) {
            db.child("user").child(uid)
                .setValue(hashMapOf("name" to username, "email" to email, "uid" to uid))
        }
    }

}