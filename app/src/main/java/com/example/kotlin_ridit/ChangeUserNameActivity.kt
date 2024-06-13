package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class ChangeUserNameActivity : AppCompatActivity() {
    private lateinit var etNewName: EditText
    private lateinit var btnChangeName: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_user_name)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        etNewName = findViewById(R.id.cambioNombreField)
        btnChangeName = findViewById(R.id.buttonChange)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


    }

    private fun initUI() {
        btnChangeName.setOnClickListener() {
            val newUsername = etNewName.text.toString()
            val user = auth.currentUser
            val userId = FirebaseAuth.getInstance().currentUser?.email.toString()
            if (newUsername.isNotEmpty()) {
                if (user != null) {
                    // Actualizar el nombre de usuario en Firestore
                    val userRef = Firebase.firestore.collection("users").document(userId)
                    userRef.update("username",newUsername)
                        .addOnSuccessListener {
                            Log.d("UPDATE", "Username updated successfully")
                            Toast.makeText(
                                baseContext,
                                "Username updated successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        .addOnFailureListener { e ->
                            Log.w("UPDATE", "Error updating username", e)
                            Toast.makeText(
                                baseContext,
                                "Failed to update username.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                } else {
                    Toast.makeText(
                        baseContext,
                        "No user is signed in.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Please enter a valid username.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            startActivity(Intent(this, PersonalDataActivity::class.java))
        }

    }
}
