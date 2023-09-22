package com.example.readradar.service

import com.example.readradar.model.User
import com.example.readradar.model.dto.RegisterDTO
import com.example.readradar.repository.RoleRepository
import com.example.readradar.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): User? {
        if (username != null) {
            return userRepository.findBy_username(username)
        }
        throw UsernameNotFoundException("Username is null")
    }

    fun register(registerDTO: RegisterDTO) {
        val user = User(
            _username = registerDTO.username,
            _password = registerDTO.password,
            email = registerDTO.email,
            profilePicture = registerDTO.profilePicture,
            role = roleRepository.findById(registerDTO.roleId).orElseThrow { throw Exception("Role not found") }
        )
        userRepository.save(user)
    }
}