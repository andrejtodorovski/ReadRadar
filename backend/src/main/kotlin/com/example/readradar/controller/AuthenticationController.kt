package com.example.readradar.controller

import com.example.readradar.configuration.JwtUtil
import com.example.readradar.service.MyUserDetailsService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authenticate")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: MyUserDetailsService
) {

    @PostMapping
    fun createAuthenticationToken(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authenticationRequest.username, authenticationRequest.password)
        )

        val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
        val jwt = jwtUtil.generateToken(userDetails!!.username)

        return ResponseEntity.ok(AuthenticationResponse(jwt))
    }
}

data class AuthenticationRequest(val username: String, val password: String)
data class AuthenticationResponse(val jwt: String)
