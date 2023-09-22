package com.example.readradar.model.dto

data class UserInfo(
    val id: Long,
    val email: String,
    val username: String,
    val numberOfReviews: Int,
    val mostReviewedCategory: String,
    val profilePicture: String,
    val role: String,
) {
}