package com.example.readradar.test.integration

import com.example.readradar.model.dto.AddReviewDTO
import com.example.readradar.repository.BookRepository
import com.example.readradar.repository.ReviewRepository
import com.example.readradar.repository.RoleRepository
import com.example.readradar.repository.UserRepository
import com.example.readradar.service.ReviewService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Sql(scripts = ["classpath:test-data/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = ["classpath:test-data/create.sql", "classpath:test-data/init.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewServiceITest {

    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository


    @Test
    fun `when adding review to already reviewed book by the user, it should throw an exception`() {
        val reviewDTO = AddReviewDTO(rating = 4.5, comment = "Great book!")
        val userId = 10L
        val bookId = 20L

        assertThrows<Exception> {
            reviewService.save(reviewDTO, userId, bookId)
        }

    }

    @Test
    fun `when saving a review, it should return the saved review and update the book average rating`() {
        val reviewDTO = AddReviewDTO(rating = 4.5, comment = "Great book!", reviewId = 2L)

        val user = userRepository.findById(1L).get()
        val book = bookRepository.findById(10L).get()

        var isReviewedByUser = reviewService.checkIfBookReviewedByUser(book.id!!, user.id!!)

        assertFalse(isReviewedByUser)

        val review = reviewService.save(reviewDTO, user.id!!, book.id!!)

        assertNotNull(review)

        isReviewedByUser = reviewService.checkIfBookReviewedByUser(book.id!!, user.id!!)

        assertTrue(isReviewedByUser)

        val updatedBook = bookRepository.findById(book.id!!).orElseThrow()

        assertEquals(4.5, updatedBook.averageRating)
    }

    @Test
    fun `deleting a review also updates the books averageRating`() {
        val user = userRepository.findById(1L).get()
        val book = bookRepository.findById(10L).get()
        val review = reviewService.save(
            AddReviewDTO(4.5, "Great book!"),
            user.id!!,
            book.id!!
        )
        val reviewId = review.id!!
        val bookId = book.id!!
        val userId = user.id!!
        val isReviewedByUser = reviewService.checkIfBookReviewedByUser(bookId, userId)

        assertTrue(isReviewedByUser)

        val reviewedBook = bookRepository.findById(bookId).orElseThrow()

        assertEquals(4.5, reviewedBook.averageRating)

        reviewService.deleteById(reviewId)

        val updatedBook = bookRepository.findById(bookId).orElseThrow()

        assertEquals(0.0, updatedBook.averageRating)
    }
}