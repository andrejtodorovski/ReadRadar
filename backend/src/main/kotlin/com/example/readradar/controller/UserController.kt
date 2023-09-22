package com.example.readradar.controller

import com.example.readradar.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/stats")
    fun getStatsForUsers() = userService.getStatsForUsers()

    @GetMapping("/current")
    fun getCurrentUser() = userService.getCurrentUser()
}