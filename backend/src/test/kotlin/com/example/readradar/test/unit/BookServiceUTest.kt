package com.example.readradar.test.unit

import com.example.readradar.exception.BookIsbnAlreadyExistsException
import com.example.readradar.exception.BookNameAlreadyExistsException
import com.example.readradar.exception.CannotChangeBookIsbnException
import com.example.readradar.exception.CannotChangeBookTitleException
import com.example.readradar.model.Book
import com.example.readradar.model.BookCategory
import com.example.readradar.model.Category
import com.example.readradar.model.dto.CreateBookDTO
import com.example.readradar.repository.BookCategoryRepository
import com.example.readradar.repository.BookRepository
import com.example.readradar.repository.CategoryRepository
import com.example.readradar.repository.ReviewRepository
import com.example.readradar.service.BookService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class BookServiceUTest {

    @MockK
    private lateinit var bookRepository: BookRepository

    @MockK
    private lateinit var bookCategoryRepository: BookCategoryRepository

    @MockK
    private lateinit var categoryRepository: CategoryRepository

    @MockK
    private lateinit var reviewRepository: ReviewRepository

    @SpyK
    @InjectMockKs
    private lateinit var bookService: BookService

    init {
        MockKAnnotations.init(this)
    }

    // creating a book
    @Test
    fun `test saveOrUpdate for new book`() {
        val bookDTO = CreateBookDTO(
            title = "Existing Title1",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            categoryIds = emptyList()
        )

        every { bookRepository.findByTitle(any()) } returns null
        every { bookRepository.findByIsbn(any()) } returns null
        every { bookRepository.save(any()) } returns mockk()
        every { categoryRepository.findAllById(any()) } returns emptyList()

        bookService.saveOrUpdate(bookDTO)

        verify(exactly = 1) { bookRepository.save(any()) }
    }

    @Test
    fun `test saveOrUpdate for new book with already existing title`() {
        val bookDTO = CreateBookDTO(
            title = "Existing Title1",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            categoryIds = emptyList()
        )

        every { bookRepository.findByTitle(any()) } returns mockk()

        assertThrows<BookNameAlreadyExistsException> {
            bookService.saveOrUpdate(bookDTO)
        }


        verify(exactly = 0) { bookRepository.save(any()) }
    }

    @Test
    fun `test saveOrUpdate for new book with already existing isbn`() {
        val bookDTO = CreateBookDTO(
            title = "Existing Title1",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            categoryIds = emptyList()
        )

        every { bookRepository.findByTitle(any()) } returns null
        every { bookRepository.findByIsbn(any()) } returns mockk()

        assertThrows<BookIsbnAlreadyExistsException> {
            bookService.saveOrUpdate(bookDTO)
        }


        verify(exactly = 0) { bookRepository.save(any()) }
    }

    @Test
    fun `test saveOrUpdate with valid category IDs`() {
        val categoryIds = listOf(1L, 2L)
        val bookDTO = CreateBookDTO(
            title = "New Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            categoryIds = categoryIds
        )

        every { bookRepository.findByTitle(any()) } returns null
        every { bookRepository.findByIsbn(any()) } returns null
        every { categoryRepository.findAllById(categoryIds) } returns listOf(
            Category(id = 1L, name = "Category 1"), Category(id = 2L, name = "Category 2")
        )
        every { bookRepository.save(any()) } returns mockk(relaxed = true)
        every { bookCategoryRepository.save(any()) } returns mockk(relaxed = true)
        every { categoryRepository.findById(any()) } returns Optional.of(mockk())


        val result = bookService.saveOrUpdate(bookDTO)

        assertNotNull(result)

        verify(exactly = 1) { bookRepository.save(any()) }

    }

    // updating a book

    @Test
    fun `test saveOrUpdate to update book details`() {
        val existingBookId = 1L
        val bookToUpdate = Book(
            id = existingBookId,
            title = "Old Title",
            author = "Old Author",
            isbn = "1234567890",
            description = "Old Description",
            coverImage = "old_image.jpg"
        )
        val bookDTO = CreateBookDTO(
            title = "Old Title",
            author = "New Author",
            isbn = "1234567890",
            description = "New Description",
            coverImage = "new_image.jpg",
            categoryIds = emptyList()
        )

        every { bookRepository.findById(existingBookId) } returns Optional.of(bookToUpdate)
        every { bookRepository.findByTitle(any()) } returns null
        every { bookRepository.findByIsbn(any()) } returns null
        every { categoryRepository.findAllById(listOf()) } returns listOf()
        every { bookRepository.save(any()) } returns Book(
            id = existingBookId,
            title = "Old Title",
            author = "New Author",
            isbn = "1234567890",
            description = "New Description",
            coverImage = "new_image.jpg"
        )
        every { bookCategoryRepository.deleteByBookId(any()) } returns Unit

        val result = bookService.saveOrUpdate(bookDTO, existingBookId)

        assertEquals("New Author", result.author)
        assertEquals("New Description", result.description)

        verify(exactly = 1) { bookRepository.save(any()) }
        verify(exactly = 1) { bookCategoryRepository.deleteByBookId(any()) }

    }

    @Test
    fun `test saveOrUpdate to update book title`() {
        val existingBookId = 1L
        val bookToUpdate = Book(
            id = existingBookId,
            title = "Old Title",
            author = "Old Author",
            isbn = "1234567890",
            description = "Old Description",
            coverImage = "old_image.jpg"
        )
        val bookDTO = CreateBookDTO(
            title = "New Title",
            author = "Old Author",
            isbn = "1234567890",
            description = "Old Description",
            coverImage = "old_image.jpg",
            categoryIds = emptyList()
        )

        every { bookRepository.findById(existingBookId) } returns Optional.of(bookToUpdate)
        every { bookRepository.findByTitle(any()) } returns null
        every { bookRepository.findByIsbn(any()) } returns null
        every { categoryRepository.findAllById(listOf()) } returns listOf()
        every { bookRepository.save(any()) } returns mockk(relaxed = true)
        every { bookCategoryRepository.deleteByBookId(any()) } returns Unit

        assertThrows<CannotChangeBookTitleException> {
            bookService.saveOrUpdate(bookDTO, existingBookId)
        }

        verify(exactly = 0) { bookRepository.save(any()) }

    }

    @Test
    fun `test saveOrUpdate to update book ISBN`() {
        val existingBookId = 1L
        val bookToUpdate = Book(
            id = existingBookId,
            title = "Old Title",
            author = "Old Author",
            isbn = "1234567890",
            description = "Old Description",
            coverImage = "old_image.jpg"
        )
        val bookDTO = CreateBookDTO(
            title = "Old Title",
            author = "Old Author",
            isbn = "0987654321",
            description = "Old Description",
            coverImage = "old_image.jpg",
            categoryIds = emptyList()
        )

        every { bookRepository.findById(existingBookId) } returns Optional.of(bookToUpdate)
        every { bookRepository.findByTitle(any()) } returns null
        every { bookRepository.findByIsbn(any()) } returns null
        every { categoryRepository.findAllById(listOf()) } returns listOf()
        every { bookRepository.save(any()) } returns mockk(relaxed = true)
        every { bookCategoryRepository.deleteByBookId(any()) } returns Unit

        assertThrows<CannotChangeBookIsbnException> {
            bookService.saveOrUpdate(bookDTO, existingBookId)
        }

        verify(exactly = 0) { bookRepository.save(any()) }

    }

    // delete

    @Test
    fun `test deleteById`() {
        val bookId = 10L

        every { bookCategoryRepository.findByBookId(bookId) } returns listOf(mockk())
        every { bookCategoryRepository.delete(any()) } returns Unit
        every { reviewRepository.findByBookId(bookId) } returns listOf(mockk())
        every { reviewRepository.delete(any()) } returns Unit
        every { bookRepository.deleteById(bookId) } returns Unit

        bookService.deleteById(bookId)

        verify(exactly = 1) { bookCategoryRepository.delete(any()) }
        verify(exactly = 1) { reviewRepository.delete(any()) }
        verify(exactly = 1) { bookRepository.deleteById(bookId) }
    }

    // find by

    @Test
    fun `test findByTitle with matching books`() {
        val book = Book(
            id = 1L,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg"
        )

        every { bookRepository.findByTitleContainingIgnoreCase("Some") } returns listOf(book)

        val result = bookService.findByTitle("Some")

        assertTrue(result.isNotEmpty())
        assertEquals(book, result[0])
    }

    @Test
    fun `test findByTitle with no matching books`() {
        every { bookRepository.findByTitleContainingIgnoreCase("Some") } returns listOf()

        val result = bookService.findByTitle("Some")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test findByAuthor with matching books`() {
        val book = Book(
            id = 1L,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg"
        )

        every { bookRepository.findByAuthorContainingIgnoreCase("Author") } returns listOf(book)

        val result = bookService.findByAuthor("Author")

        assertTrue(result.isNotEmpty())
        assertEquals(book, result[0])
    }

    @Test
    fun `test findByAuthor with no matching books`() {
        every { bookRepository.findByAuthorContainingIgnoreCase("Author") } returns listOf()

        val result = bookService.findByAuthor("Author")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test findByCategory with matching books`() {
        val book = Book(
            id = 1L,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg"
        )
        val category = Category(
            id = 1L,
            name = "Category"
        )
        val bookCategory = BookCategory(
            book = book,
            category = category
        )

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf(bookCategory)

        val result = bookService.findByCategory(category.id)

        assertTrue(result.isNotEmpty())
        assertEquals(book, result[0])
    }

    @Test
    fun `test findByCategory with no matching books`() {

        val category = Category(
            id = 1L,
            name = "Category"
        )

        every { bookCategoryRepository.findByCategoryId(category.id) } returns listOf()

        val result = bookService.findByCategory(category.id)

        assertTrue(result.isEmpty())
    }

    // usage of slot to check the viewCount update
    @Test
    fun `test findById updates viewCount`() {
        val bookId = 1L
        val book = Book(
            id = bookId,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            viewCount = 0
        )

        every { bookRepository.findById(bookId) } returns Optional.of(book)

        val slot = slot<Book>()
        every { bookRepository.save(capture(slot)) } answers { slot.captured }

        val result = bookService.findById(1L)

        assertEquals(slot.captured.viewCount, result.viewCount)
    }

    @Test
    fun `test updateBookViewCount with existing book`() {
        val bookId = 1L
        val initialViewCount = 5L
        val book = Book(
            id = bookId,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            viewCount = initialViewCount
        )

        every { bookRepository.findById(bookId) } returns Optional.of(book)
        every { bookRepository.save(any()) } answers { firstArg() }

        val updatedBook = bookService.updateBookViewCount(bookId)

        assertEquals(initialViewCount + 1, updatedBook.viewCount)
        verify { bookRepository.save(updatedBook) }
    }

    //  Predicate coverage RCCC 2,3,4,6

    @Test
    fun `test isBookATopRomanceBook for case 2`() {
        val book = Book(
            id = 1L,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            averageRating = 4.00,
            viewCount = 999
        )
        val category = Category(
            id = 1L, name = "Romance"
        )
        val bookCategory = BookCategory(
            book = book, category = category
        )

        every { bookCategoryRepository.findByBookId(book.id!!) } returns listOf(bookCategory)

        val result = bookService.isBookATopRomanceBook(book)

        assertTrue(result)
    }

    @Test
    fun `test isBookATopRomanceBook for case 3`() {
        val book = Book(
            id = 1L,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            averageRating = 2.5,
            viewCount = 1000
        )
        val category = Category(
            id = 1L, name = "Romance"
        )
        val bookCategory = BookCategory(
            book = book, category = category
        )

        every { bookCategoryRepository.findByBookId(book.id!!) } returns listOf(bookCategory)

        val result = bookService.isBookATopRomanceBook(book)

        assertTrue(result)
    }

    @Test
    fun `test isBookATopRomanceBook for case 4`() {
        val book = Book(
            id = 1L,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            averageRating = 1.5,
            viewCount = 100
        )
        val category = Category(
            id = 1L, name = "Romance"
        )
        val bookCategory = BookCategory(
            book = book, category = category
        )

        every { bookCategoryRepository.findByBookId(book.id!!) } returns listOf(bookCategory)

        val result = bookService.isBookATopRomanceBook(book)

        assertFalse(result)
    }

    @Test
    fun `test isBookATopRomanceBook for case 6`() {
        val book = Book(
            id = 1L,
            title = "Some Title",
            author = "Author",
            isbn = "1234567890",
            description = "Description",
            coverImage = "image.jpg",
            averageRating = 4.5,
            viewCount = 100
        )
        val category = Category(
            id = 1L, name = "Drama"
        )
        val bookCategory = BookCategory(
            book = book, category = category
        )

        every { bookCategoryRepository.findByBookId(book.id!!) } returns listOf(bookCategory)

        val result = bookService.isBookATopRomanceBook(book)

        assertFalse(result)
    }


}
