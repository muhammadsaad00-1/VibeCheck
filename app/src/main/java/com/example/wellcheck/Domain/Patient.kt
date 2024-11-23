package com.example.wellcheck.Domain

data class Patient( val name: String = "",
                    val age: String = "",
                    val gender: String = "",
                    val contactInfo: String = "",
                    val medicalHistory: Map<String, String> = emptyMap()
)
