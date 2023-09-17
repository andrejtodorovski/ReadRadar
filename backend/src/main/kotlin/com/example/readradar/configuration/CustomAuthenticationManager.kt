package com.example.readradar.configuration

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationManager : AuthenticationManager {
    override fun authenticate(authentication: Authentication?): Authentication {
        return authentication!!
    }
}