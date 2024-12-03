package com.example.wellcheck.Activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Domain.Patient
import com.example.wellcheck.Domain.PillReminder
import com.example.wellcheck.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddPillReminderActivity : AppCompatActivity() {

    private lateinit var etPillName: EditText
    private lateinit var etPillQuantity: EditText
    private lateinit var etPillDuration: EditText
    private lateinit var etPillFrequency: EditText
    private lateinit var btnSetStartTime: Button
    private lateinit var btnAddReminder: Button

    private var startTime: Long = 0
    private lateinit var currentPatient: Patient // Logged-in patient

    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Initialize FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pill_reminder)

        // Initialize UI components
        etPillName = findViewById(R.id.etPillName)
        etPillQuantity = findViewById(R.id.etPillQuantity)
        etPillDuration = findViewById(R.id.etPillDuration)
        etPillFrequency = findViewById(R.id.etPillFrequency)
        btnSetStartTime = findViewById(R.id.btnSetStartTime)
        btnAddReminder = findViewById(R.id.btnAddReminder)

        // Fetch current logged-in user's ID
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val patientId = currentUser.uid // Get patient ID from the authenticated user
            loadPatientData(patientId)
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
            finish() // Exit if no user is logged in
        }

        // Set listeners
        btnSetStartTime.setOnClickListener { pickStartTime() }
        btnAddReminder.setOnClickListener { addReminder() }
    }

    private fun loadPatientData(patientId: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("patients").child(patientId).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val patient = dataSnapshot.getValue(Patient::class.java)
                    if (patient != null) {
                        currentPatient = patient
                        currentPatient.id = patientId
                        // You can now safely access the pillReminders map
                    } else {
                        Toast.makeText(this, "Failed to parse patient data.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Patient data not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load patient data.", Toast.LENGTH_SHORT).show()
                finish() // Exit the activity if patient data cannot be loaded
            }
    }

    private fun pickStartTime() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                startTime = calendar.timeInMillis
                Toast.makeText(this, "Start time set!", Toast.LENGTH_SHORT).show()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun addReminder() {
        val pillName = etPillName.text.toString().trim()
        val quantity = etPillQuantity.text.toString().toIntOrNull() ?: 0
        val duration = etPillDuration.text.toString().toIntOrNull() ?: 0
        val frequency = etPillFrequency.text.toString().toIntOrNull() ?: 0

        // Validate input
        if (pillName.isEmpty() || quantity <= 0 || duration <= 0 || frequency <= 0 || startTime == 0L) {
            Toast.makeText(this, "Please fill all fields correctly!", Toast.LENGTH_SHORT).show()
            return
        }

        // Create PillReminder object
        val reminder = PillReminder(
            pillName = pillName,
            quantity = quantity,
            duration = duration,
            frequency = frequency,
            startTime = startTime
        )

        // Add reminder to patient's list (using a map)
        val reminderId = "reminder" + System.currentTimeMillis()  // Use a unique key
        val updatedPillReminders = currentPatient.pillReminders.toMutableMap()
        updatedPillReminders[reminderId] = reminder
        currentPatient.pillReminders = updatedPillReminders

        // Save updated patient data to Firebase
        val database = FirebaseDatabase.getInstance().reference
        database.child("patients").child(currentPatient.id).setValue(currentPatient)
            .addOnSuccessListener {
                Toast.makeText(this, "Reminder added successfully!", Toast.LENGTH_SHORT).show()
                finish() // Close activity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add reminder.", Toast.LENGTH_SHORT).show()
            }
    }
}