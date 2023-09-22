package com.example.readradar.controller

import com.example.readradar.configuration.JwtUtil
import com.example.readradar.model.dto.LoginDTO
import com.example.readradar.model.dto.LoginResponse
import com.example.readradar.model.dto.RegisterDTO
import com.example.readradar.repository.RoleRepository
import com.example.readradar.service.MyUserDetailsService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: MyUserDetailsService,
    private val roleRepository: RoleRepository
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginDTO): ResponseEntity<Any> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)
        val jwt = jwtUtil.generateToken(userDetails!!.username)
        jwtUtil.validateToken(jwt, userDetails.username)
        val roles: List<String> = userDetails.authorities.stream()
            .map { obj: GrantedAuthority? -> obj!!.authority }!!.toList()
        val loginResponse = LoginResponse(jwt, userDetails.username, roles[0])
        return ResponseEntity.ok().body(loginResponse)
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Any> {
        return ResponseEntity.ok()
            .body(("You have been logged out"))
    }

    @PostMapping("/register")
    fun register(@RequestBody registerDTO: RegisterDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(userDetailsService.register(registerDTO))
    }

    @PostMapping("/init-roles")
    fun createRole(): ResponseEntity<Any> {
        roleRepository.saveAll(
            listOf(
                com.example.readradar.model.Role(1, "USER"),
                com.example.readradar.model.Role(2, "ADMIN")
            )
        )
        return ResponseEntity.ok().body("Roles created")
    }
}
