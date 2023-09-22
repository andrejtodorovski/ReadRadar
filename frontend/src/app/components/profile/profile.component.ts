import { Component, OnInit } from '@angular/core';
import { UserInfo } from 'src/app/models/userInfo';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userInfo: UserInfo | null = null;
  constructor(private userService: UserService) { }
  ngOnInit(): void {
    this.getCurrentUser();
  }
  getCurrentUser(): void {
    this.userService.getCurrentUser().subscribe({
      next: (response) => {
        this.userInfo = response;
      }
    });
  }
}
