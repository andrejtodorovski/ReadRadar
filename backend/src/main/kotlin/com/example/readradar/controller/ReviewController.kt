package com.example.readradar.controller

import com.example.readradar.model.User
import com.example.readradar.model.dto.AddReviewDTO
import com.example.readradar.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {
    @GetMapping
    fun findAll(): ResponseEntity<Any> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val principal: Any? = authentication.principal
        return if (principal is User) {
            ResponseEntity.ok(reviewService.findByUserId(principal.id!!))
        } else {
            ResponseEntity.badRequest().body("User not found")
        }
    }

    @GetMapping("/{id}")
    fun findByBookId(@PathVariable id: Long) = reviewService.findByBookId(id)

    @GetMapping("/check/{id}")
    fun checkIfBookReviewedByUser(@PathVariable id: Long): ResponseEntity<Any> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val principal: Any? = authentication.principal
        return if (principal is User) {
            ResponseEntity.ok(reviewService.checkIfBookReviewedByUser(id, principal.id!!))
        } else {
            ResponseEntity.badRequest().body("User not found")
        }
    }

    @PostMapping("/add/{id}")
    fun addReview(@PathVariable id: Long, @RequestBody reviewDTO: AddReviewDTO): ResponseEntity<Any> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val principal: Any? = authentication.principal
        return if (principal is User) {
            ResponseEntity.ok(reviewService.save(reviewDTO, principal.id!!, id))
        } else {
            ResponseEntity.badRequest().body("User not found")
        }
    }
}