package com.example.readradar.model.dto

data class AddReviewDTO(
    val rating: Double,
    val comment: String,
    val reviewId: Long? = null
)