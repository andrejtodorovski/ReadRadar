package com.example.readradar.model

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne
    @JoinColumn(name = "book_id")
    val book: Book,
    val rating: Double,
    val reviewText: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
