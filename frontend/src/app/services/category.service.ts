import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Category } from '../models/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  baseUrl = 'http://localhost:8080/api/categories'
  constructor(private httpClient: HttpClient) {}
  
  getAllCategories() {
    return this.httpClient.get<Category[]>(this.baseUrl);
  }
}
