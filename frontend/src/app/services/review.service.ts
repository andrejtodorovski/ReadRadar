import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReviewInfo } from '../models/reviewInfo';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  baseUrl = 'http://localhost:8080/api/reviews'
  constructor(private httpClient: HttpClient) {}

  getReviewsForUser() {
    return this.httpClient.get<ReviewInfo[]>(this.baseUrl);
  }

  getReviewsForBook(id: number) {
    return this.httpClient.get<ReviewInfo[]>(this.baseUrl + '/' + id);
  }

  addReview(id: number, reviewDTO: any) {
    return this.httpClient.post(this.baseUrl + '/add/' + id, reviewDTO);
  }

  checkIfBookReviewedByUser(id: number) {
    return this.httpClient.get<boolean>(this.baseUrl + '/check/' + id);
  }

}
