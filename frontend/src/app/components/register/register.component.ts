import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RegisterDTO } from 'src/app/models/registerDTO';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  registerDTO: RegisterDTO = {
    username: '',
    password: '',
    email: '',
    profilePicture: '',
    roleId: 1,
  };
  roles: any[] = [
    { id: 1, name: 'User' },
    { id: 2, name: 'Admin' },
  ]
  constructor(private accountService: AccountService, private router: Router, private toastrService: ToastrService) {}
  ngOnInit(): void {}
  register() {
    this.accountService.register(this.registerDTO).subscribe({
      next: () => {
        this.router.navigateByUrl('/login');
      },
      error: (err) => {
        this.toastrService.error(err.error);
      }
    });
  }
}
