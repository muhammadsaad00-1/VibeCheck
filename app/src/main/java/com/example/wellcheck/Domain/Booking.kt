package com.example.wellcheck.Domain

data class Booking(
    val bookingId: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    var doctorName: String ="",
    val date: String = "",
    val time: String = "",
    var status: String = "Pending" // Default status is "Pending"
)
