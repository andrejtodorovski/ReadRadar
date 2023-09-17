package com.example.readradar.exception

class BookIsbnAlreadyExistsException(isbn: String) : RuntimeException("Book with ISBN '$isbn' already exists.")
