package com.example.wellcheck.Domain

data class FileModel(
    val fileName: String,
    val fileUrl: String,
    val title: String?,
    val uploadDate: String?,
    val note: String?
)

