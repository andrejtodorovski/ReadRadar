package com.example.readradar.test.integration

import com.example.readradar.exception.BookIsbnAlreadyExistsException
import com.example.readradar.exception.BookNameAlreadyExistsException
import com.example.readradar.exception.CannotChangeBookIsbnException
import com.example.readradar.exception.CannotChangeBookTitleException
import com.example.readradar.model.dto.CreateBookDTO
import com.example.readradar.repository.BookCategoryRepository
import com.example.readradar.repository.BookRepository
import com.example.readradar.repository.CategoryRepository
import com.example.readradar.repository.ReviewRepository
import com.example.readradar.service.BookService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Sql(scripts = ["classpath:test-data/reset.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(
    scripts = ["classpath:test-data/create.sql", "classpath:test-data/init.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookServiceITest {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var bookCategoryRepository: BookCategoryRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var bookService: BookService

    @Test
    fun `when getting all books, it should return all books`() {
        val books = bookService.findAll()
        assertTrue(books.size == 2)
    }

    @Test
    fun `when getting all books by title, it should return all books by title`() {
        val books = bookService.findByTitle("2")
        assertTrue(books.size == 1)
    }

    @Test
    fun `when getting all books by author, it should return all books by author`() {
        val books = bookService.findByAuthor("Author2")
        assertTrue(books.size == 1)
    }

    @Test
    fun `when getting all books by category, it should return all books by category`() {
        val books = bookService.findByCategory(10L)
        assertTrue(books.size == 1)
    }

    @Test
    fun `when getting top romance books, should return the romance book`() {
        val books = bookService.getTopRomanceBooks()
        assertTrue(bookCategoryRepository.findByBookId(books[0].id!!)[0].category.name == "Romance")
        assertTrue(books[0].averageRating >= 4.00 || books[0].viewCount >= 1000)
        assertTrue(books.size == 1)
    }

    @Test
    fun `when creating new book, should save the book`() {
        val book = bookService.saveOrUpdate(
            CreateBookDTO(
                title = "New Book",
                author = "New Author",
                isbn = "123456789555",
                description = "New Description",
                coverImage = "image.jpg",
                categoryIds = emptyList()
            )
        )
        assertNotNull(book)
        assertNotNull(book.id)
    }

    @Test
    fun `when updating an existing book, should update the book`() {
        val initialBook = bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Book To Be Updated",
                author = "Initial Author",
                isbn = "123456789312",
                description = "Initial Description",
                coverImage = "image.jpg",
                categoryIds = emptyList()
            )
        )

        val updatedBook = bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Book To Be Updated",
                author = "Updated Author",
                isbn = "123456789312",
                description = "Updated Description",
                coverImage = "image.jpg",
                categoryIds = emptyList()
            ),
            initialBook.id!!
        )

        val fetchedBook = bookService.findByTitle("Book To Be Updated").firstOrNull()
        assertNotNull(fetchedBook)
        assertEquals("Updated Author", fetchedBook?.author)
        assertEquals("Updated Description", fetchedBook?.description)
    }


    @Test
    fun `when creating a book with existing name, it should throw an exception`() {
        assertThrows<BookNameAlreadyExistsException> {
            bookService.saveOrUpdate(
                CreateBookDTO(
                    title = "Test Book",
                    author = "Author",
                    isbn = "1234567890",
                    description = "Description",
                    coverImage = "image.jpg",
                    categoryIds = emptyList()
                )
            )
        }
    }

    @Test
    fun `when creating a book with existing isbn, it should throw an exception`() {
        assertThrows<BookIsbnAlreadyExistsException> {
            bookService.saveOrUpdate(
                CreateBookDTO(
                    title = "Test Book 3",
                    author = "Author",
                    isbn = "123456789321",
                    description = "Description",
                    coverImage = "image.jpg",
                    categoryIds = emptyList()
                )
            )
        }
    }

    @Test
    fun `when updating a books name, it should throw an exception`() {
        assertThrows<CannotChangeBookTitleException> {
            bookService.saveOrUpdate(
                CreateBookDTO(
                    title = "Test Book321123",
                    author = "Author",
                    isbn = "123456789321",
                    description = "Description",
                    coverImage = "image.jpg",
                    categoryIds = emptyList()
                ),
                10L
            )
        }
    }

    @Test
    fun `when updating a books isbn, it should throw an exception`() {
        assertThrows<CannotChangeBookIsbnException> {
            bookService.saveOrUpdate(
                CreateBookDTO(
                    title = "Test Book",
                    author = "Author",
                    isbn = "123321",
                    description = "Description",
                    coverImage = "image.jpg",
                    categoryIds = emptyList()
                ),
                10L
            )
        }
    }
}