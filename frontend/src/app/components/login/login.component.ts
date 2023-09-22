import { Component, OnInit } from '@angular/core';
import { LoginDTO } from '../../models/loginDTO';
import { AccountService } from 'src/app/services/account.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginDTO: LoginDTO = {
    username: '',
    password: '',
  };

  constructor(private accountService: AccountService, private router: Router, private toastrService: ToastrService) {}
  ngOnInit(): void {}

  login() {
    this.accountService.login(this.loginDTO).subscribe({
      next: () => {
        this.router.navigateByUrl('/');
      },
      error: (err) => {        
        this.toastrService.error(err.error);
      }
    });
  }
}
