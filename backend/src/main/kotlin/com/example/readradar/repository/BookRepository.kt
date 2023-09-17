package com.example.readradar.repository

import com.example.readradar.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {

    fun findByTitleContainingIgnoreCase(title: String): List<Book>

    fun findByAuthorContainingIgnoreCase(author: String): List<Book>
    fun findByIsbn(isbn: String): Book?
    fun findByTitle(title: String): Book?
}
