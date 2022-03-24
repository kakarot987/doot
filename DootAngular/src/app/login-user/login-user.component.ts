import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from "../service/token-storage.service";
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-login-user',
  templateUrl: './login-user.component.html',
  styleUrls: ['./login-user.component.css']
})
export class LoginUserComponent implements OnInit {

  constructor(private router: Router,private tokenStorage:TokenStorageService) { }

  ngOnInit(): void {
  }


}
