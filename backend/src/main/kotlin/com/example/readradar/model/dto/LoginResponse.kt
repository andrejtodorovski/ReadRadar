package com.example.readradar.model.dto

data class LoginResponse(
    val token: String,
    val username: String,
    val role: String
) {
}