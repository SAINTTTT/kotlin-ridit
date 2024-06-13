package com.example.kotlin_ridit

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import android.content.Intent

class PersonalDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)
        val db = Firebase.firestore

        // Obtener referencias a los elementos de la interfaz
        val tvUsername: TextView = findViewById(R.id.tvUsername)
        val tvEmail: TextView = findViewById(R.id.tvEmail)
        val btnChangeEmail: Button = findViewById(R.id.btnChangeEmail)
        val btnChangeUserName: Button = findViewById(R.id.changeUserNameButton)

        val userId = FirebaseAuth.getInstance().currentUser?.email.toString()

        val docRef= db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tvUsername.text = document.data?.get("username").toString()
                    tvEmail.text = document.id
                } else {
                    Log.d(TAG, "No such document")
                }
            }
        btnChangeUserName.setOnClickListener {

            startActivity(Intent(this, ChangeUserNameActivity::class.java))
        }

        // Configurar el botón para cambiar el email
//        btnChangeEmail.setOnClickListener {
//            // Chequear cual es la mejor forma de cambiar el mail actual de un usuario
//            val intent = Intent(this, ChangeEmailActivity::class.java)
//            startActivity(intent)
//        }
    }


}