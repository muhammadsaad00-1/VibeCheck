package com.example.wellcheck.Activity

import android.content.Intent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.R
import com.google.firebase.auth.FirebaseAuth
class Fpass : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Firebase Authentication instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fpass)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Get references to UI elements
        val btnResetPassword = findViewById<Button>(R.id.btn_reset_password)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val btnGoBack = findViewById<Button>(R.id.btn_Go_back) // Reference to the Go Back button

        // Reset Password Button Click
        btnResetPassword.setOnClickListener {
            val email = etEmail.text.toString().trim() // Get the entered email

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                // Send reset password email
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Reset password email sent! Check your inbox.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Error: ${task.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }


        btnGoBack.setOnClickListener {

            val intent = Intent(this, com.example.wellcheck.Activity.Intro::class.java)
            startActivity(intent)
            finish() // Finish the current activity
        }
    }
}