package com.example.readradar.repository

import com.example.readradar.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByNameContainingIgnoreCase(name: String): List<Category>

}
