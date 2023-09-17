package com.example.readradar.repository

import com.example.readradar.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findBy_username(username: String): User?
    fun findByEmail(email: String): User?

    fun existsBy_username(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}
