import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Category } from 'src/app/models/category';
import { AccountService } from 'src/app/services/account.service';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit{
  ngOnInit(): void {
    this.loadCategories()
    this.getCurrentUser()
  }
  constructor(private accountService: AccountService,
    private categoryService: CategoryService,
    private router: Router, private toastrService: ToastrService){}
  loggedIn: boolean = false
  isAdmin: boolean = false
  categories: Category[] = []

  loadCategories(){
    this.categoryService.getAllCategories().subscribe({
      next: (response) => {
        this.categories = response
      }
    })
  }

  logout(){
    this.accountService.logout()
    this.router.navigateByUrl('/')
    this.toastrService.success('Logged out successfully')
  }

  getCurrentUser() {
    return this.accountService.currentUser$.subscribe({
      next: (response) => {        
        this.isAdmin = response?.role === 'ADMIN';
        this.loggedIn = !!response;
      },
    });
  }
}
