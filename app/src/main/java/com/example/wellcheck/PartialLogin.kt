package com.example.wellcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Domain.Doctors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PartialLogin : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partial_login)

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPass = findViewById(R.id.edt_pass)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPass.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val uid = mAuth.currentUser?.uid
                    if (uid != null) {
                        fetchDoctorData(uid)
                    } else {
                        Toast.makeText(this, "Failed to retrieve user ID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Login failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchDoctorData(userId: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("doctors").child(userId)
        databaseRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val doctor = snapshot.getValue(Doctors::class.java)
                if (doctor != null) {
                    // Navigate to the `west` activity and pass the doctor data
                   // val doctor = Doctors("Dr. John", "Biography", "Address", "Specialty", "Location", "Mobile", "Patients", "Website", "www.ggogle.com", 3.5, 10.0)
                    val intent = Intent(this, west::class.java)
                    intent.putExtra("doctor", doctor) // Make sure you are passing a Doctors object
                    startActivity(intent)


                    finish()
                } else {
                    Toast.makeText(this, "Doctor data is empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Doctor not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error fetching doctor data: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
