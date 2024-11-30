package com.example.wellcheck.Domain

data class IssueModel(
    val id: String = "",
    val description: String = "",
    val email: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
