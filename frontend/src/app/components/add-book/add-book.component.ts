import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Category } from 'src/app/models/category';
import { CreateBookDTO } from 'src/app/models/createBookDTO';
import { BookService } from 'src/app/services/book.service';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent implements OnInit {
  book: CreateBookDTO = {
    title: '',
    isbn: '',
    author: '',
    coverImage: '',
    description: '',
    categoryIds: []
  }
  categories: Category[] = []
  selectedCategoryId: number[] = []

  constructor(private bookService: BookService, private categoryService: CategoryService, 
    private router: Router, private toastrService: ToastrService) { }

  ngOnInit(): void {
    this.loadCategories()
  }

  loadCategories(){
    this.categoryService.getAllCategories().subscribe({
      next: (response) => {
        this.categories = response
      }
    })
  }
  onSubmit(){
    this.book.categoryIds = this.selectedCategoryId
    this.bookService.createBook(this.book).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/books')
        this.toastrService.success('Book created successfully')
      },
      error: (err) => {
        this.toastrService.error(err.error)
      }
    })
  }
}