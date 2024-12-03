package com.example.wellcheck.Domain



data class Patient(
    var id: String = "", // Unique ID for each patient (e.g., from Firebase or other authentication)
    val name: String = "",
    val age: String = "",
    val gender: String = "",
    val contactInfo: String = "",
    val medicalHistory: Map<String, String> = emptyMap(),
    var pillReminders: Map<String, PillReminder> = emptyMap()  // List of pill reminders for the patient
)