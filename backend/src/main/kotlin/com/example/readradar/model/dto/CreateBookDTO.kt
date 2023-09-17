package com.example.readradar.model.dto

data class CreateBookDTO(
    val title: String,
    val isbn: String,
    val author: String,
    val coverImage: String,
    val description: String,
    val categoryIds: List<Long>
)
