import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Book } from 'src/app/models/book';
import { BookService } from 'src/app/services/book.service';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent {
  books: Book[] = [];

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    const categoryId = this.route.snapshot.paramMap.get('categoryId');    
    if (categoryId) {
      this.loadBooksByCategory(+categoryId);
    }
    else {
      this.loadAllBooks();
    }
  };


  loadBooksByCategory(categoryId: number): void {
    this.bookService.getBooksByCategory(categoryId).subscribe(data => {
      this.books = data;
    });
  }

  loadAllBooks(): void {
    this.bookService.getAllBooks().subscribe(data => {
      this.books = data;
    });
  }
}
