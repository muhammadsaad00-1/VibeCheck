package com.example.wellcheck.Domain

data class HealthTip(
    val text: String? = null,      // The health tip text
    val timestamp: Long? = null   // Timestamp for when the tip was posted
)
