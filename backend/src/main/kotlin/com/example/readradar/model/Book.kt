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
    val description: String,
    val averageRating: Double? = null,
    val price: Double? = null,// ideata beshe Sale na site ljubovni knigi so malku reviews primer neso taka
)
