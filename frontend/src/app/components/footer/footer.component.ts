import { Component } from '@angular/core';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  loggedIn = false;
  ngOnInit(): void {
    this.getCurrentUser();
  }
  constructor(private accountService: AccountService) {}
  logout() {
    this.accountService.logout();
  }
  getCurrentUser() {
    return this.accountService.currentUser$.subscribe({
      next: (response) => {
        this.loggedIn = !!response;
      },
    });
  }
}
