package com.example.readradar.service

import com.example.readradar.model.User
import com.example.readradar.model.dto.UserInfo
import com.example.readradar.repository.BookCategoryRepository
import com.example.readradar.repository.ReviewRepository
import com.example.readradar.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val bookCategoryRepository: BookCategoryRepository
) {

    fun findAll(): List<User> = userRepository.findAll()

    fun findByUsername(username: String): User? = userRepository.findBy_username(username)

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun existsByUsername(username: String): Boolean = userRepository.existsBy_username(username)

    fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    fun save(user: User): User = userRepository.save(user)

    fun deleteById(id: Long) = userRepository.deleteById(id)

    fun mapUserToUserInfo(user: User): UserInfo {
        return UserInfo(id = user.id!!,
            email = user.email,
            username = user.username,
            numberOfReviews = reviewRepository.findByUserId(user.id).size,
            mostReviewedCategory =
            when {
                reviewRepository.findByUserId(user.id).isNotEmpty() -> {
                    reviewRepository.findByUserId(user.id).flatMap { review ->
                        bookCategoryRepository.findByBookId(review.book.id!!)
                    }.groupBy {
                        it.category
                    }.mapValues { (_, value) ->
                        value.size
                    }.maxByOrNull { entry ->
                        entry.value
                    }?.key?.name!!
                }

                else -> {
                    "No reviews"
                }
            },
            profilePicture = "",
            role = user.role.name
        )
    }

    fun getStatsForUsers(): List<UserInfo> {
        return userRepository.findAll().map {
            mapUserToUserInfo(it)
        }

    }

    fun getCurrentUser(): UserInfo {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val principal: Any? = authentication.principal
        return if (principal is User) {
            mapUserToUserInfo(principal)
        } else {
            throw Exception("User not found")
        }
    }
}
