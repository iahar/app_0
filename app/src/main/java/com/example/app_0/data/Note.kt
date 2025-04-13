package com.example.app_0.data

data class Note(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)