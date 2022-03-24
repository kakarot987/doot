import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from "../service/token-storage.service";
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router,private tokenStorage:TokenStorageService) { }

  ngOnInit(): void {
  }
  routeToMail(){
    this.router.navigate(['/sendMail']);

  }
  routeToFAQ(){
    this.router.navigate(['/faq']);

  }
  routeToPremium(){
    this.router.navigate(['/premium']);

  }
  logout(): void {
    console.log("inside logout")
    this.tokenStorage.signOut();
    this.reloadPage();
  }
  reloadPage(): void {
    window.location.reload();
  }
}
