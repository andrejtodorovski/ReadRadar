import { Component, OnInit } from '@angular/core';
import { JwtAuth } from './models/jwtAuth';
import { AccountService } from './services/account.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'frontend';
  constructor(private accountService: AccountService) {}
  ngOnInit(): void {
    this.setCurrentUser();
  }
  setCurrentUser() {
    const userString = localStorage.getItem('user');
    if (!userString) {
      return;
    }
    const user: JwtAuth = JSON.parse(userString);
    this.accountService.setCurrentUser(user);
  }
}
