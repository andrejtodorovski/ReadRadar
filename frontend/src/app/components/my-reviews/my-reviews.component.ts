import { Component, OnInit } from '@angular/core';
import { ReviewInfo } from 'src/app/models/reviewInfo';
import { ReviewService } from 'src/app/services/review.service';

@Component({
  selector: 'app-my-reviews',
  templateUrl: './my-reviews.component.html',
  styleUrls: ['./my-reviews.component.css']
})
export class MyReviewsComponent implements OnInit {
  reviews: ReviewInfo[] = [];
  constructor(private reviewService: ReviewService) { }
  ngOnInit(): void {
    this.loadReviews();
  }
  loadReviews(): void {
    this.reviewService.getReviewsForUser().subscribe(data => {
      this.reviews = data;
    });
  }
}
