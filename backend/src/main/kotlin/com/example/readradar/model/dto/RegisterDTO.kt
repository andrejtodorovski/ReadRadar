package com.example.readradar.model.dto

data class RegisterDTO(
    val username: String,
    val password: String,
    val email: String,
    val profilePicture: String,
    val roleId: Long
) {
}