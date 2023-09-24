package com.example.readradar.test.unit

import com.example.readradar.model.Book
import com.example.readradar.model.Review
import com.example.readradar.model.User
import com.example.readradar.model.dto.AddReviewDTO
import com.example.readradar.repository.BookRepository
import com.example.readradar.repository.ReviewRepository
import com.example.readradar.repository.RoleRepository
import com.example.readradar.repository.UserRepository
import com.example.readradar.service.MyUserDetailsService
import com.example.readradar.service.ReviewService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class ReviewServiceUTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var reviewRepository: ReviewRepository

    @MockK
    private lateinit var bookRepository: BookRepository


    @SpyK
    @InjectMockKs
    private lateinit var reviewService: ReviewService

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should return all reviews`() {
        val reviews = listOf(mockk<Review>())
        every { reviewRepository.findAll() } returns reviews

        val result = reviewService.findAll()

        assertEquals(reviews, result)
    }

    @Test
    fun `should return reviews by user id`() {
        val reviews = listOf(mockk<Review>())
        every { reviewRepository.findByUserId(1L) } returns reviews

        val result = reviewService.findByUserId(1L)

        assertEquals(reviews, result)
    }

    @Test
    fun `should return reviews by book id`() {
        val reviews = listOf(mockk<Review>())
        every { reviewRepository.findByBookId(1L) } returns reviews

        val result = reviewService.findByBookId(1L)

        assertEquals(reviews, result)
    }

    @Test
    fun `should return latest reviews`() {
        val reviews = listOf(mockk<Review>())
        every { reviewRepository.findTop10ByOrderByTimestampDesc() } returns reviews

        val result = reviewService.findLatestReviews()

        assertEquals(reviews, result)
    }

    @Test
    fun `should save review and return it`() {
        val reviewDTO = AddReviewDTO(4.5, "Great Book!")
        val book = mockk<Book>()
        val user = mockk<User>()
        every { bookRepository.findById(1L) } returns Optional.of(book)
        every { userRepository.findById(1L) } returns Optional.of(user)
        every { reviewService.checkIfBookReviewedByUser(1L, 1L) } returns false
        every { reviewService.updateBookAverageRating(1L, 4.5) } just Runs
        every { reviewRepository.save(any()) } returns mockk()

        assertDoesNotThrow {
            reviewService.save(reviewDTO, 1L, 1L)
        }

        verify(exactly = 1) { reviewRepository.save(any()) }
    }

    @Test
    fun `should not save review`() {
        val reviewDTO = AddReviewDTO(4.5, "Great Book!")
        val book = mockk<Book>()
        val user = mockk<User>()
        every { bookRepository.findById(1L) } returns Optional.of(book)
        every { userRepository.findById(1L) } returns Optional.of(user)
        every { reviewService.checkIfBookReviewedByUser(1L, 1L) } returns true

        assertThrows<Exception> {
            reviewService.save(reviewDTO, 1L, 1L)
        }

        verify(exactly = 0) { reviewRepository.save(any()) }
    }

    @Test
    fun `test updateBookAverageRating`() {
        val book = mockk<Book>()
        val review = mockk<Review>()
        every { bookRepository.findById(1L) } returns Optional.of(book)
        every { reviewRepository.findByBookId(1L) } returns listOf()
        every { review.rating } returns 4.5
        every { book.averageRating = any() } just Runs
        every { bookRepository.save(any()) } returns mockk()

        assertDoesNotThrow {
            reviewService.updateBookAverageRating(1L, 4.5)
        }

        verify(exactly = 1) { bookRepository.save(any()) }
    }


}