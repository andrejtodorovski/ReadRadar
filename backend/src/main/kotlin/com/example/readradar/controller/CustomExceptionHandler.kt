package com.example.readradar.controller

import com.example.readradar.exception.*
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(BookNameAlreadyExistsException::class)
    fun handleNameExistsException(e: BookNameAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(BookIsbnAlreadyExistsException::class)
    fun handleIsbnExistsException(e: BookIsbnAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFoundException(e: BookNotFoundException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(CannotChangeBookTitleException::class)
    fun handleCannotChangeBookTitleException(e: CannotChangeBookTitleException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(CannotChangeBookIsbnException::class)
    fun handleCannotChangeBookIsbnException(e: CannotChangeBookIsbnException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(e: BadCredentialsException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}
