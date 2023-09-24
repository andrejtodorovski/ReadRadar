package com.example.readradar.service

import com.example.readradar.exception.BookNotFoundException
import com.example.readradar.model.Review
import com.example.readradar.model.dto.AddReviewDTO
import com.example.readradar.repository.BookRepository
import com.example.readradar.repository.ReviewRepository
import com.example.readradar.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) {

    fun findAll(): List<Review> = reviewRepository.findAll()

    fun findByUserId(userId: Long): List<Review> = reviewRepository.findByUserId(userId)

    fun findByBookId(bookId: Long): List<Review> = reviewRepository.findByBookId(bookId)

    fun findLatestReviews(): List<Review> = reviewRepository.findTop10ByOrderByTimestampDesc()

    fun save(reviewDTO: AddReviewDTO, userId: Long, bookId: Long): Review {
        if (checkIfBookReviewedByUser(bookId, userId))
            throw Exception("Book already reviewed by user")

        updateBookAverageRating(bookId, reviewDTO.rating)
        return reviewRepository.save(
            Review(
                id = reviewDTO.reviewId,
                book = bookRepository.findById(bookId).get(),
                user = userRepository.findById(userId).get(),
                rating = reviewDTO.rating,
                comment = reviewDTO.comment
            )
        )
    }

    fun updateBookAverageRating(id: Long, newRating: Double, deletingRating: Boolean = false) {
        val book = bookRepository.findById(id).orElseThrow { throw BookNotFoundException(id) }
        val sumOfRatings = reviewRepository.findByBookId(id).sumOf { it.rating }
        val ratingCount = reviewRepository.findByBookId(id).count() + 1
        book.averageRating = if (!deletingRating)
            (sumOfRatings + newRating) / ratingCount
        else
            sumOfRatings / ratingCount
        bookRepository.save(book)
    }

    fun deleteById(id: Long) {
        reviewRepository.findById(id).also {
            reviewRepository.deleteById(it.get().id!!)
            updateBookAverageRating(it.get().book.id!!, 0.0, true)
        }
    }

    fun checkIfBookReviewedByUser(bookId: Long, userId: Long): Boolean {
        return reviewRepository.findByBookIdAndUserId(bookId, userId) != null
    }
}
