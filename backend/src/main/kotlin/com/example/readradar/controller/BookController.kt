package com.example.readradar.controller

import com.example.readradar.model.dto.CreateBookDTO
import com.example.readradar.service.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {
    @GetMapping
    fun findAll() = bookService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = bookService.findById(id)

    @GetMapping("/title")
    fun findByTitle(@RequestParam title: String) = bookService.findByTitle(title)

    @GetMapping("/author")
    fun findByAuthor(@RequestParam author: String) = bookService.findByAuthor(author)

    @GetMapping("/category")
    fun findByCategory(@RequestParam categoryId: Long) = bookService.findByCategory(categoryId)

    @PostMapping
    fun save(@RequestBody createBookDTO: CreateBookDTO) = bookService.saveOrUpdate(createBookDTO)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = bookService.deleteById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody createBookDTO: CreateBookDTO) = bookService.saveOrUpdate(createBookDTO, id)

    @GetMapping("/recommendations/{userId}")
    fun getRecommendations(@PathVariable userId: Long) = bookService.recommendBooksForUser(userId)
}