package com.example.readradar.test.unit

import com.example.readradar.model.User
import com.example.readradar.repository.BookCategoryRepository
import com.example.readradar.repository.ReviewRepository
import com.example.readradar.repository.UserRepository
import com.example.readradar.service.UserService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class UserServiceUTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var reviewRepository: ReviewRepository

    @MockK
    private lateinit var bookCategoryRepository: BookCategoryRepository


    @SpyK
    @InjectMockKs
    private lateinit var userService: UserService

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test findAll`() {
        every { userRepository.findAll() } returns listOf(mockk<User>())

        val result = userService.findAll()

        assertEquals(1, result.size)
    }

    @Test
    fun `test findByUsername`() {
        val username = "testUsername"
        every { userRepository.findBy_username(username) } returns mockk<User>()

        val result = userService.findByUsername(username)

        assertNotNull(result)
    }

    @Test
    fun `test findByUsername returns null`() {
        val username = "testUsername"
        every { userRepository.findBy_username(username) } returns null

        val result = userService.findByUsername(username)

        assertNull(result)
    }

    @Test
    fun `test findByEmail`() {
        every { userRepository.findByEmail("email@mail.com") } returns mockk<User>()

        val result = userService.findByEmail("email@mail.com")

        assertNotNull(result)
    }

    @Test
    fun `test findByEmail returns null`() {
        every { userRepository.findByEmail("email@mail.com") } returns null

        val result = userService.findByEmail("email@mail.com")

        assertNull(result)
    }

    @Test
    fun `test existsByUsername`() {
        val username = "testUsername"
        every { userRepository.existsBy_username(username) } returns true

        val result = userService.existsByUsername(username)

        assertTrue(result)
    }

    @Test
    fun `test doesn't existsByUsername`() {
        val username = "testUsername"
        every { userRepository.existsBy_username(username) } returns false

        val result = userService.existsByUsername(username)

        assertFalse(result)
    }

    @Test
    fun `test existsByEmail`() {
        every { userRepository.existsByEmail("email@mail.com") } returns true
        val result = userService.existsByEmail("email@mail.com")

        assertTrue(result)
    }

    @Test
    fun `test doesn't existsByEmail`() {
        every { userRepository.existsByEmail("email@mail.com") } returns false
        val result = userService.existsByEmail("email@mail.com")

        assertFalse(result)
    }

    @Test
    fun `test save`() {
        val user = mockk<User>()
        every { userRepository.save(user) } returns user

        val result = userService.save(user)

        assertEquals(user, result)
    }

    @Test
    fun `test deleteById`() {
        val id = 1L
        every { userRepository.deleteById(id) } returns Unit

        userService.deleteById(id)

        verify(exactly = 1) { userRepository.deleteById(id) }
    }


}