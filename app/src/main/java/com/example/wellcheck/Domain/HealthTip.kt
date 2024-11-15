package com.example.wellcheck.Domain
import android.os.Parcel
import android.os.Parcelable

data class HealthTip(
    val text: String? = null,      // The health tip text
    val timestamp: Long? = null   // Timestamp for when the tip was posted
)
