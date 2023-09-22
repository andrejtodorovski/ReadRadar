import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Book } from '../models/book';
import { CreateBookDTO } from '../models/createBookDTO';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  baseUrl = 'http://localhost:8080/api/books'
  constructor(private httpClient: HttpClient) {}

  getBooksByCategory(categoryId: number) {
    return this.httpClient.get<Book[]>(this.baseUrl + '/category?categoryId=' + categoryId);
  }

  getAllBooks() {
    return this.httpClient.get<Book[]>(this.baseUrl);
  }

  createBook(book: CreateBookDTO) {
    return this.httpClient.post<any>(this.baseUrl, book);
  }

  getBookById(id: number) {
    return this.httpClient.get<Book>(this.baseUrl + '/' + id);
  }

}
