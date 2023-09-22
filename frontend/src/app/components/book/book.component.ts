import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { of, switchMap } from 'rxjs';
import { Book } from 'src/app/models/book';
import { ReviewDTO } from 'src/app/models/reviewDTO';
import { ReviewInfo } from 'src/app/models/reviewInfo';
import { AccountService } from 'src/app/services/account.service';
import { BookService } from 'src/app/services/book.service';
import { ReviewService } from 'src/app/services/review.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css'],
})
export class BookComponent implements OnInit {
  book: Book | null = null;
  reviews: ReviewInfo[] = [];
  reviewDTO: ReviewDTO = {
    rating: 0,
    comment: '',
  };
  loggedIn = false;

  constructor(
    private bookService: BookService,
    private reviewService: ReviewService,
    private route: ActivatedRoute,
    private toastrService: ToastrService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.loadBook();
    this.loadReviews();
    this.getCurrentUser();
  }

  getCurrentUser() {
    return this.accountService.currentUser$.subscribe({
      next: (response) => {        
        this.loggedIn = !!response;
      },
    });
  }

  loadBook(): void {
    const id = this.route.snapshot.paramMap.get('id')!!;
    this.bookService.getBookById(+id).subscribe((data) => {
      this.book = data;
    });
  }
  loadReviews(): void {
    const id = this.route.snapshot.paramMap.get('id')!!;
    this.reviewService.getReviewsForBook(+id).subscribe((data) => {
      this.reviews = data;
    });
  }

  isStarFilledRating(rating: number, starNumber: number): boolean {
    return starNumber <= rating;
  }

  addReview(): void {
    const id = +this.route.snapshot.paramMap.get('id')!!;

    this.reviewService
      .checkIfBookReviewedByUser(id)
      .pipe(
        switchMap((data) => {
          if (data) {
            this.toastrService.warning('You have already reviewed this book!');
            return of(null);
          } else {
            return this.reviewService.addReview(id, this.reviewDTO);
          }
        })
      )
      .subscribe((data) => {
        if (data) {
          this.loadReviews();
          this.toastrService.success('Review added successfully!');
        }
      });
  }
}
