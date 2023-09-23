package com.example.readradar.model

import jakarta.persistence.*

@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val isbn: String,
    val author: String,
    val coverImage: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    var averageRating: Double = 0.0,
    var viewCount: Long = 0
)
