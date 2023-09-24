package com.example.readradar.test.unit

import com.example.readradar.model.Book
import com.example.readradar.model.BookCategory
import com.example.readradar.model.Category
import com.example.readradar.model.dto.CategoryStats
import com.example.readradar.repository.BookCategoryRepository
import com.example.readradar.repository.CategoryRepository
import com.example.readradar.service.CategoryService
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.just
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CategoryServiceUTest {

    @MockK
    private lateinit var bookCategoryRepository: BookCategoryRepository

    @MockK
    private lateinit var categoryRepository: CategoryRepository


    @SpyK
    @InjectMockKs
    private lateinit var categoryService: CategoryService

    init {
        MockKAnnotations.init(this)
    }


    // Graph coverage
    @Test
    fun `test categoryStats5`() {
        // [1,2,3,5,8,2,3,5,8,2,9,11]
        val category = Category(1L, "Fantasy")
        val book1 = Book(
            1L, "The Hobbit", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 4.5, 100
        )
        val book2 = Book(
            2L, "The Lord of the Rings", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 4.5, 100
        )

        val bookCategory1 = BookCategory(1L, book1, category)
        val bookCategory2 = BookCategory(1L, book2, category)

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf(bookCategory1, bookCategory2)

        val categoryStats = categoryService.calculateStatsForCategory(category)

        val expectedCategoryStats = CategoryStats(
            name = "Fantasy",
            booksCount = 2,
            booksAverageRating = 4.5,
            booksAverageViewCount = 100.0
        )

        assertEquals(expectedCategoryStats, categoryStats)
    }


    @Test
    fun `test categoryStats6`() {
        // [1,2,3,4,5,8,2,3,5,8,2,9,11]
        val category = Category(1L, "Fantasy")
        val book1 = Book(
            1L, "Harry Potter", "1234567890", "J.K. Rowling", "cover.jpg", "Good book", 4.5, 100
        )
        val book2 = Book(
            2L, "The Lord of the Rings", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 4.5, 100
        )

        val bookCategory1 = BookCategory(1L, book1, category)
        val bookCategory2 = BookCategory(1L, book2, category)

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf(bookCategory1, bookCategory2)

        val categoryStats = categoryService.calculateStatsForCategory(category)

        val expectedCategoryStats = CategoryStats(
            name = "Fantasy",
            booksCount = 2,
            booksAverageRating = 4.5,
            booksAverageViewCount = 101.5
        )

        assertEquals(expectedCategoryStats, categoryStats)
    }

    @Test
    fun `test categoryStats8`() {
        // [1,2,3,5,6,8,2,3,5,8,2,9,11]
        val category = Category(1L, "Fantasy")
        val book1 = Book(
            1L, "The Hobbit", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 0.5, 100
        )
        val book2 = Book(
            2L, "The Lord of the Rings", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 4.5, 100
        )

        val bookCategory1 = BookCategory(1L, book1, category)
        val bookCategory2 = BookCategory(1L, book2, category)

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf(bookCategory1, bookCategory2)

        val categoryStats = categoryService.calculateStatsForCategory(category)

        val expectedCategoryStats = CategoryStats(
            name = "Fantasy",
            booksCount = 2,
            booksAverageRating = 2.25,
            booksAverageViewCount = 100.0
        )

        assertEquals(expectedCategoryStats, categoryStats)
    }

    @Test
    fun `test categoryStats9`() {
        // [1,2,3,5,6,7,8,2,3,5,8,2,9,11]
        val category = Category(1L, "Fantasy")
        val book1 = Book(
            1L, "The Hobbit", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 0.5, 3
        )
        val book2 = Book(
            2L, "The Lord of the Rings", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 4.5, 100
        )

        val bookCategory1 = BookCategory(1L, book1, category)
        val bookCategory2 = BookCategory(1L, book2, category)

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf(bookCategory1, bookCategory2)

        val categoryStats = categoryService.calculateStatsForCategory(category)

        val expectedCategoryStats = CategoryStats(
            name = "Fantasy",
            booksCount = 2,
            booksAverageRating = 2.25,
            booksAverageViewCount = 50.0
        )

        assertEquals(expectedCategoryStats, categoryStats)
    }

    @Test
    fun `test categoryStats11`() {
        // [1,2,9,10,11]
        val category = Category(1L, "Fantasy")

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf()

        val categoryStats = categoryService.calculateStatsForCategory(category)

        val expectedCategoryStats = CategoryStats(
            name = "Fantasy",
            booksCount = 0,
            booksAverageRating = 0.0,
            booksAverageViewCount = 0.0
        )

        assertEquals(expectedCategoryStats, categoryStats)
    }

    @Test
    fun `test categoryStats13`() {
        // [1,2,3,4,5,6,8,2,3,5,8,2,9,11]
        val category = Category(1L, "Fantasy")
        val book1 = Book(
            1L, "Harry Potter", "1234567890", "J.K. Rowling", "cover.jpg", "Good book", 0.5, 100
        )
        val book2 = Book(
            2L, "The Lord of the Rings", "1234567890", "J.R.R. Tolkien", "cover.jpg", "Good book", 4.5, 100
        )

        val bookCategory1 = BookCategory(1L, book1, category)
        val bookCategory2 = BookCategory(1L, book2, category)

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf(bookCategory1, bookCategory2)

        val categoryStats = categoryService.calculateStatsForCategory(category)

        val expectedCategoryStats = CategoryStats(
            name = "Fantasy",
            booksCount = 2,
            booksAverageRating = 2.25,
            booksAverageViewCount = 101.5
        )

        assertEquals(expectedCategoryStats, categoryStats)
    }

    @Test
    fun `test findAll`() {
        val category1 = Category(1L, "Fantasy")
        val category2 = Category(2L, "Horror")
        val category3 = Category(3L, "Romance")

        every { categoryRepository.findAll() } returns listOf(category1, category2, category3)

        val categories = categoryService.findAll()

        assertEquals(3, categories.size)
        assertEquals(category1, categories[0])
        assertEquals(category2, categories[1])
        assertEquals(category3, categories[2])
    }

    @Test
    fun `test findByName`() {
        val category1 = Category(1L, "Fantasy")
        val category2 = Category(2L, "Horror")
        val category3 = Category(3L, "Romance")

        every { categoryRepository.findByNameContainingIgnoreCase("fan") } returns listOf(category1)

        val categories = categoryService.findByName("fan")

        assertEquals(1, categories.size)
        assertTrue(categories.contains(category1))
    }

    @Test
    fun `test save`() {
        val category = Category(1L, "Fantasy")

        every { categoryRepository.save(category) } returns category

        val savedCategory = categoryService.save(category)

        assertEquals(category, savedCategory)
    }

    @Test
    fun `test delete`() {
        val category = Category(1L, "Fantasy")

        every { categoryRepository.deleteById(category.id) } just Runs

        categoryService.deleteById(category.id)
    }
}