package com.example.wellcheck.Domain


data class PillReminder(
    val pillName: String = "",
    val quantity: Int = 0,
    val duration: Int = 0,
    val frequency: Int = 0,
    val startTime: Long = 0L
)