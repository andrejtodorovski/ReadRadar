package com.example.readradar.model.dto

data class CategoryStats(
    val name: String,
    val booksCount: Long,
    val booksAverageRating: Double,
    val booksAverageViewCount: Double,
) {
}