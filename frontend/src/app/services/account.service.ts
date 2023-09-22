import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDTO } from '../models/loginDTO';
import { BehaviorSubject, map } from 'rxjs';
import { RegisterDTO } from '../models/registerDTO';
import { JwtAuth } from '../models/jwtAuth';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  baseUrl = 'http://localhost:8080/api/auth';
  private currentUserSource = new BehaviorSubject<JwtAuth | null>(null);
  currentUser$ = this.currentUserSource.asObservable();
  constructor(private httpClient: HttpClient) {}

  login(loginDTO: LoginDTO) {
    return this.httpClient
      .post<JwtAuth>(this.baseUrl + '/login', loginDTO)
      .pipe(
        map((response: JwtAuth) => {
          const user = response;
          if (user) {            
            localStorage.setItem('user', JSON.stringify(user));
            this.currentUserSource.next(user);
          }
        })
      );
  }

  setCurrentUser(user: JwtAuth) {
    this.currentUserSource.next(user);
  }

  logout() {
    localStorage.removeItem('user');
    this.currentUserSource.next(null);
  }
  register(registerDTO: RegisterDTO) {
    return this.httpClient
      .post(this.baseUrl + '/register', registerDTO)
  }
}
