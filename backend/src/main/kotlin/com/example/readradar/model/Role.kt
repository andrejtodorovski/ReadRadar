package com.example.readradar.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
) : GrantedAuthority {
    override fun getAuthority(): String {
        return name
    }
}
