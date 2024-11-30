package com.example.wellcheck.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Domain.IssueModel
import com.example.wellcheck.R
import com.google.firebase.database.FirebaseDatabase

class Issue : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue)

        val editDescription = findViewById<EditText>(R.id.edit_issue_description)
        val editEmail = findViewById<EditText>(R.id.edit_user_email)
        val btnSubmit = findViewById<Button>(R.id.btn_submit_issue)

        btnSubmit.setOnClickListener {
            val description = editDescription.text.toString().trim()
            val email = editEmail.text.toString().trim()

            if (description.isBlank()) {
                Toast.makeText(this, "Please enter a description of the issue.", Toast.LENGTH_SHORT).show()
            } else if (email.isBlank()) {
                Toast.makeText(this, "Please provide your email.", Toast.LENGTH_SHORT).show()
            } else {
                submitIssue(description, email)
                // Clear the fields
                editDescription.text.clear()
                editEmail.text.clear()
            }
        }
    }

    private fun submitIssue(description: String, email: String) {
        // Reference to Firebase Database
        val databaseRef = FirebaseDatabase.getInstance().getReference("issues")

        // Create a unique ID for the issue
        val issueId = databaseRef.push().key ?: return

        // Create an IssueModel object
        val issue = IssueModel(
            id = issueId,
            description = description,
            email = email
        )

        // Save the issue in the database
        databaseRef.child(issueId).setValue(issue)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Issue reported successfully. Thank you!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to report the issue. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
