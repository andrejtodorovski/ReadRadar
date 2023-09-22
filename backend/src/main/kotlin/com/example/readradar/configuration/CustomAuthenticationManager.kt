package com.example.readradar.configuration

import com.example.readradar.model.User
import com.example.readradar.service.MyUserDetailsService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationManager(
    val userDetailsService: MyUserDetailsService
) : AuthenticationManager {
    override fun authenticate(authentication: Authentication?): Authentication {
        val username: String = authentication!!.name
        val password: String = authentication.credentials.toString()
        if ("" == username || "" == password) {
            throw BadCredentialsException("Invalid Credentials")
        }
        val user: User = userDetailsService.loadUserByUsername(username) ?: throw BadCredentialsException("User not found!")
        if (user.password != password) {
            throw BadCredentialsException("Password is incorrect!")
        }
        return UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
    }
}