package com.example.readradar.service

import com.example.readradar.model.User
import com.example.readradar.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): User? {
        if (username != null) {
            return userRepository.findBy_username(username)
        }
        throw UsernameNotFoundException("Username is null")
    }
}