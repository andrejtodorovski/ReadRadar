package com.example.readradar.service

import com.example.readradar.model.User
import com.example.readradar.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository) {

    fun findAll(): List<User> = userRepository.findAll()

    fun findByUsername(username: String): User? = userRepository.findBy_username(username)

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun existsByUsername(username: String): Boolean = userRepository.existsBy_username(username)

    fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    fun save(user: User): User = userRepository.save(user)

    fun deleteById(id: Long) = userRepository.deleteById(id)
}
