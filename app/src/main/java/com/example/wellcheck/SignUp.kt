package com.example.wellcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Domain.Patient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtName: EditText
    private lateinit var edtAge: EditText
    private lateinit var edtGender: EditText
    private lateinit var edtContact: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        edtName = findViewById(R.id.name)
        edtAge = findViewById(R.id.age)
        edtGender = findViewById(R.id.gender)
        edtContact = findViewById(R.id.contact)
        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.pass)
        btnSignUp = findViewById(R.id.btn_signUp)

        // Set onClickListener for the Sign-Up button
        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val pass = edtPassword.text.toString().trim()

            if (validateInputs(email, pass)) {
                signUp(email, pass)
            }
        }
    }

    private fun signUp(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val patient = Patient(
                        name = edtName.text.toString(),
                        age = edtAge.text.toString(),
                        gender = edtGender.text.toString(),
                        contactInfo = edtContact.text.toString()
                    )
                    if (uid != null) {
                        FirebaseDatabase.getInstance().reference
                            .child("patients")
                            .child(uid)
                            .setValue(patient)
                            .addOnCompleteListener {
                                Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, Login::class.java))
                                finish() // Prevent returning to this screen
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Database Error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateInputs(email: String, pass: String): Boolean {
        if (edtName.text.isEmpty()) {
            edtName.error = "Name is required"
            return false
        }
        if (edtAge.text.isEmpty()) {
            edtAge.error = "Age is required"
            return false
        }
        if (edtGender.text.isEmpty()) {
            edtGender.error = "Gender is required"
            return false
        }
        if (edtContact.text.isEmpty()) {
            edtContact.error = "Contact information is required"
            return false
        }
        if (email.isEmpty()) {
            edtEmail.error = "Email is required"
            return false
        }
        if (pass.isEmpty()) {
            edtPassword.error = "Password is required"
            return false
        }
        if (pass.length < 6) {
            edtPassword.error = "Password must be at least 6 characters long"
            return false
        }
        return true
    }
}
