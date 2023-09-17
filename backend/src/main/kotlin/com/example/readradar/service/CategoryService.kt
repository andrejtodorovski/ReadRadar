package com.example.readradar.service

import com.example.readradar.model.Category
import com.example.readradar.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun findAll(): List<Category> = categoryRepository.findAll()

    fun findByName(name: String): List<Category> = categoryRepository.findByNameContainingIgnoreCase(name)

    fun save(category: Category): Category = categoryRepository.save(category)

    fun deleteById(id: Long) = categoryRepository.deleteById(id)
}

