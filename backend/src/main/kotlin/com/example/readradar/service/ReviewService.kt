package com.example.readradar.service

import com.example.readradar.model.Review
import com.example.readradar.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewService(private val reviewRepository: ReviewRepository) {

    fun findAll(): List<Review> = reviewRepository.findAll()

    fun findByUserId(userId: Long): List<Review> = reviewRepository.findByUserId(userId)

    fun findByBookId(bookId: Long): List<Review> = reviewRepository.findByBookId(bookId)

    fun findLatestReviews(): List<Review> = reviewRepository.findTop10ByOrderByTimestampDesc()

    fun save(review: Review): Review = reviewRepository.save(review)

    fun deleteById(id: Long) = reviewRepository.deleteById(id)
}
