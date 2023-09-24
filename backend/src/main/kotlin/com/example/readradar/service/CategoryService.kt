package com.example.readradar.service

import com.example.readradar.model.Category
import com.example.readradar.model.dto.CategoryStats
import com.example.readradar.repository.BookCategoryRepository
import com.example.readradar.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val bookCategoryRepository: BookCategoryRepository
) {

    fun findAll(): List<Category> = categoryRepository.findAll()

    fun findByName(name: String): List<Category> = categoryRepository.findByNameContainingIgnoreCase(name)

    fun save(category: Category): Category = categoryRepository.save(category)

    fun deleteById(id: Long) {
        bookCategoryRepository.deleteByCategoryId(id).also {
            categoryRepository.deleteById(id)
        }
    }

    fun statsForCategories(): List<CategoryStats> = findAll().map { category ->
        calculateStatsForCategory(category)
    }

    // Graph Coverage
    fun calculateStatsForCategory(category: Category): CategoryStats {
        var bookCount: Long = 0
        var booksAverageRating: Double = 0.0
        var booksAverageViewCount: Double = 0.0
        val books = bookCategoryRepository.findByCategoryId(category.id).map { bookCategory ->
            bookCategory.book
        }
        for (book in books) {
            bookCount += 1
            booksAverageRating += book.averageRating
            booksAverageViewCount += book.viewCount
            if (book.author == "J.K. Rowling") {
                booksAverageViewCount += 3
            }
            if (book.averageRating < 1.0) {
                booksAverageRating -= book.averageRating
                if (book.viewCount < 5) {
                    booksAverageViewCount -= book.viewCount
                }
            }
        }
        booksAverageRating /= bookCount
        booksAverageViewCount /= bookCount
        if (bookCount < 2) {
            booksAverageRating = 0.0
            booksAverageViewCount = 0.0
        }
        return CategoryStats(category.name, bookCount, booksAverageRating, booksAverageViewCount)
    }
}

