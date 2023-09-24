package com.example.readradar.repository

import com.example.readradar.model.BookCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookCategoryRepository : JpaRepository<BookCategory, Long> {

    fun findByBookId(bookId: Long): List<BookCategory>

    fun findByCategoryId(categoryId: Long): List<BookCategory>

    fun deleteByBookId(bookId: Long)
    fun deleteByCategoryId(categoryId: Long)
}
