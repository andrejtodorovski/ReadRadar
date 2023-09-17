package com.example.readradar.model

import jakarta.persistence.*

@Entity
data class BookCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    val book: Book,
    @ManyToOne
    val category: Category,
)