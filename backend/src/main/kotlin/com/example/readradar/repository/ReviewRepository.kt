package com.example.readradar.repository

import com.example.readradar.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {

    fun findByUserId(userId: Long): List<Review>

    fun findByBookId(bookId: Long): List<Review>

    fun findTop10ByOrderByTimestampDesc(): List<Review>
    fun findByBookIdAndRatingGreaterThanEqual(bookId: Long, rating: Double): List<Review>
    fun findByUserIdAndRatingGreaterThanEqual(userId: Long, rating: Double): List<Review>
}
