import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserInfo } from '../models/userInfo';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseUrl = 'http://localhost:8080/api/users'
  constructor(private httpClient: HttpClient) {}

  getAllUsers() {
    return this.httpClient.get<UserInfo[]>(this.baseUrl+'/stats');
  }

  getCurrentUser() {
    return this.httpClient.get<UserInfo>(this.baseUrl+'/current');
  }
}
