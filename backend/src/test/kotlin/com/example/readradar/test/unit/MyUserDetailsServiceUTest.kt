package com.example.readradar.test.unit

import com.example.readradar.model.Role
import com.example.readradar.model.User
import com.example.readradar.model.dto.RegisterDTO
import com.example.readradar.repository.RoleRepository
import com.example.readradar.repository.UserRepository
import com.example.readradar.service.MyUserDetailsService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.UsernameNotFoundException

class MyUserDetailsServiceUTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var roleRepository: RoleRepository


    @SpyK
    @InjectMockKs
    private lateinit var myUserDetailsService: MyUserDetailsService

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test loadUserByUsername with null argument`() {
        assertThrows<UsernameNotFoundException> {
            myUserDetailsService.loadUserByUsername(null)
        }
    }

    @Test
    fun `test loadUserByUsername`() {
        val role = Role(1L, "USER")
        val user = User(
            1L, "username", "password", "email", "first name", role
        )
        every { userRepository.findBy_username(user.username) } returns user

        val returned = myUserDetailsService.loadUserByUsername(user.username)

        assertEquals(user.username, returned!!.username)
    }

    @Test
    fun `test register`() {
        val registerDTO = RegisterDTO(
            "username", "password", "email", "first.jpg", 1L
        )

        every { roleRepository.findById(1L).get() } returns mockk()
        every { userRepository.save(any()) } returns mockk()

        myUserDetailsService.register(registerDTO)

        verify(exactly = 1) {
            userRepository.save(any())
        }
    }
}