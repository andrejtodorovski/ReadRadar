package com.example.readradar.exception

class BookNotFoundException(id: Long) : RuntimeException("Book with ID '$id' does not exist.")