package com.example.wellcheck

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateProfile : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtAge: EditText
    private lateinit var edtGender: EditText
    private lateinit var edtContact: EditText
    private lateinit var btnSave: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        auth = FirebaseAuth.getInstance()
        edtName = findViewById(R.id.edtEditName)
        edtAge = findViewById(R.id.edtEditAge)
        edtGender = findViewById(R.id.edtEditGender)
        edtContact = findViewById(R.id.edtEditContact)
        btnSave = findViewById(R.id.btnSaveProfile)

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val uid = auth.currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().reference.child("patients").child(uid)
        val updatedData = mapOf(
            "name" to edtName.text.toString(),
            "age" to edtAge.text.toString(),
            "gender" to edtGender.text.toString(),
            "contactInfo" to edtContact.text.toString()
        )

        ref.updateChildren(updatedData).addOnCompleteListener {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to update profile: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}