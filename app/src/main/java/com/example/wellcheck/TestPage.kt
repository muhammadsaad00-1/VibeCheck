package com.example.wellcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TestPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvName: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvContact: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnUploadFiles: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_page)

        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        tvName = findViewById(R.id.tvName)
        tvAge = findViewById(R.id.tvAge)
        tvGender = findViewById(R.id.tvGender)
        tvContact = findViewById(R.id.tvContact)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnUploadFiles = findViewById(R.id.btnUploadFiles)

        // Load Patient Info
        loadPatientInfo()

        // Edit Profile Button Click
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, UpdateProfile::class.java))
        }

        // Upload Files Button Click
        btnUploadFiles.setOnClickListener {
            startActivity(Intent(this, UploadFiles::class.java))
        }
    }

    private fun loadPatientInfo() {
        val uid = auth.currentUser?.uid ?: return

        val ref = FirebaseDatabase.getInstance().reference.child("patients").child(uid)
        ref.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Clear existing data to prevent mixing old data with new
                tvName.text = ""
                tvAge.text = ""
                tvGender.text = ""
                tvContact.text = ""

                // Set updated values
                tvName.text = "Name: ${snapshot.child("name").value}"
                tvAge.text = "Age: ${snapshot.child("age").value}"
                tvGender.text = "Gender: ${snapshot.child("gender").value}"
                tvContact.text = "Contact Info: ${snapshot.child("contactInfo").value}"
            }
        }.addOnFailureListener {
            // Handle failure case, you might want to clear and display an error message
            tvName.text = "Failed to load data."
            tvAge.text = ""
            tvGender.text = ""
            tvContact.text = ""
        }
    }

}
