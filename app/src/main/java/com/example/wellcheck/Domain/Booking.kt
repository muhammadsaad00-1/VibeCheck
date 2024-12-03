package com.example.wellcheck.Domain

data class Booking(
    val bookingId: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    var doctorName: String ="",
    var date: String = "",
    var time: String = "",
    var status: String = "Pending" // Default status is "Pending"
)
