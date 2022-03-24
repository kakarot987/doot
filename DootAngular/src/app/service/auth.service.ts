import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isLoggedInOk : boolean;

  constructor(private http: HttpClient) { 
    this.isLoggedInOk = false;
  }

  login(email:string,phoneNumber: number ,password:string): Observable<any> {
    return this.http.post(AUTH_API + 'login', {
      email: email,
      phoneNumber : phoneNumber,
      password: password
    }, httpOptions
    );
  }
  register(name : string,phoneNumber: number, email: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      phoneNumber,
      email,
      password,
      name
    }, httpOptions);
  }
}