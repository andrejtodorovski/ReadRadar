package com.example.readradar.service

import com.example.readradar.exception.*
import com.example.readradar.model.Book
import com.example.readradar.model.BookCategory
import com.example.readradar.model.dto.CreateBookDTO
import com.example.readradar.repository.BookCategoryRepository
import com.example.readradar.repository.BookRepository
import com.example.readradar.repository.CategoryRepository
import com.example.readradar.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(
    private val bookRepository: BookRepository,
    private val bookCategoryRepository: BookCategoryRepository,
    private val categoryRepository: CategoryRepository, private val reviewRepository: ReviewRepository,
) {

    fun findAll(): List<Book> = bookRepository.findAll()

    fun findById(id: Long): Book {
        updateBookViewCount(id).also {
            return bookRepository.findById(id).orElseThrow { throw BookNotFoundException(id) }
        }
    }

    fun updateBookViewCount(id: Long) : Book{
        val book = bookRepository.findById(id).orElseThrow { throw BookNotFoundException(id) }
        book.viewCount += 1
        return bookRepository.save(book)
    }

    fun findByTitle(title: String): List<Book> = bookRepository.findByTitleContainingIgnoreCase(title)

    fun findByAuthor(author: String): List<Book> = bookRepository.findByAuthorContainingIgnoreCase(author)

    fun findByCategory(categoryId: Long): List<Book> {
        return bookCategoryRepository.findByCategoryId(categoryId).map { bookCategory ->
            bookCategory.book
        }
    }

    fun saveOrUpdate(createBookDTO: CreateBookDTO, id: Long? = null): Book {

        val existingBook = id?.let { bookRepository.findById(it).orElse(null) }

        if (existingBook == null) {
            bookRepository.findByTitle(createBookDTO.title)?.let {
                throw BookNameAlreadyExistsException(createBookDTO.title)
            }

            bookRepository.findByIsbn(createBookDTO.isbn)?.let {
                throw BookIsbnAlreadyExistsException(createBookDTO.isbn)
            }
        } else {
            if (existingBook.title != createBookDTO.title) {
                throw CannotChangeBookTitleException()
            }
            if (existingBook.isbn != createBookDTO.isbn) {
                throw CannotChangeBookIsbnException()
            }
        }

        val bookToSave = existingBook?.copy(
            title = createBookDTO.title,
            author = createBookDTO.author,
            isbn = createBookDTO.isbn,
            description = createBookDTO.description,
            coverImage = createBookDTO.coverImage
        ) ?: Book(
            title = createBookDTO.title,
            author = createBookDTO.author,
            isbn = createBookDTO.isbn,
            description = createBookDTO.description,
            coverImage = createBookDTO.coverImage
        )

        val savedBook = bookRepository.save(bookToSave)

        if (existingBook != null) {
            bookCategoryRepository.deleteByBookId(existingBook.id!!)
        }

        val validCategoryIds = categoryRepository.findAllById(createBookDTO.categoryIds).map { it.id }.toSet()

        if (validCategoryIds.size != createBookDTO.categoryIds.size) {
            throw IllegalArgumentException("Some provided category IDs are invalid.")
        }

        validCategoryIds.forEach { categoryId ->
            val category = categoryRepository.findById(categoryId).get()
            val bookCategory = BookCategory(
                book = savedBook,
                category = category
            )
            bookCategoryRepository.save(bookCategory)
        }

        return savedBook
    }


    fun deleteById(id: Long) {
        val bookCategories = bookCategoryRepository.findByBookId(id)
        bookCategories.forEach { bookCategory ->
            bookCategoryRepository.delete(bookCategory)
        }
        val reviews = reviewRepository.findByBookId(id)
        reviews.forEach { review ->
            reviewRepository.delete(review)
        }
        bookRepository.deleteById(id)
    }


    fun getTopRomanceBooks(): List<Book> {
        val books = bookRepository.findAll()
        return books.filter { isBookATopRomanceBook(it) }

    }

    fun isBookATopRomanceBook(book: Book): Boolean {
        val categories = bookCategoryRepository.findByBookId(book.id!!).map { it.category.name }
        return categories.contains("Romance") && (book.averageRating >= 4.00 || book.viewCount >= 1000)
    }

}
