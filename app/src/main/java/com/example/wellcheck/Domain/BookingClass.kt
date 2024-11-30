package com.example.wellcheck.Domain

data class BookingClass(
    val bookingId: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    val date: String = "",
    val time: String = "",
    var status: String = "Pending" // Default status is "Pending"
)

