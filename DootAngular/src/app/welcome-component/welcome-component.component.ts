import { Component, OnInit } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { TokenStorageService } from '../service/token-storage.service';
@Component({
  selector: 'app-welcome-component',
  templateUrl: './welcome-component.component.html',
  styleUrls: ['./welcome-component.component.css']
})
export class WelcomeComponentComponent implements OnInit {

  constructor(private authService: AuthService,private tokenStorage: TokenStorageService) {}
  loginPage : boolean = true
  registrationPage : boolean = false

  emailLogin : boolean = true
  phoneLogin : boolean = false

  ngOnInit(): void {
  }

  onLoginClick(){
    this.loginPage = true
    this.registrationPage = false
  }
  onRegistrationClick(){
    this.loginPage = false
    this.registrationPage = true
  }
  getCheckboxesValueEmail(){
    this.emailLogin = true
    this.phoneLogin = false
  }
  getCheckboxesValuePhone(){
    this.emailLogin = false
    this.phoneLogin = true
  }

  title = 'Doot';
  closeResult !: string;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
 
  //variables declaration for Login User
  phoneNumber !: number
  email !: string
  password !: string
  name !: string

  isUserRegistered = false
  userMessage : any

  isLoggedIn = false;
  isLoginFailed = false;
  errorMessageLogin = '';
  roles: string[] = [];

  onSubmit(data:any): void {
   this.phoneNumber = parseInt(data.phoneNumber)
   this.email = data.email
   this.password = data.password
   this.name = data.name
  console.log(this.phoneNumber,this.email,this.password)
    this.authService.register(this.name,this.phoneNumber, this.email, this.password).subscribe(
        data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      (err) => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );
  }
  onSubmitLogin(data:any):void{
    this.authService.login(data.loginEmail,data.loginPhone,data.loginPassword).subscribe(
      data =>{
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.reloadPage();
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }
}
