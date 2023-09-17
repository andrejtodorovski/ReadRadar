package com.example.readradar.exception

class BookNameAlreadyExistsException(title: String) : RuntimeException("Book with title '$title' already exists.")

