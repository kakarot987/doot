import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponentComponent } from "./welcome-component/welcome-component.component";
import { LoginUserComponent } from "./login-user/login-user.component";
import { FaqComponent } from "./faq/faq.component";
import { PremiumComponent } from "./premium/premium.component";
import { SendMailComponent } from "./send-mail/send-mail.component";

const routes: Routes = [
  {path: '', component: WelcomeComponentComponent},
  {     
      path: 'home', component:WelcomeComponentComponent
  },
    {path: 'login', component:LoginUserComponent },
    {path: 'faq' , component: FaqComponent},
    {path : 'premium' , component : PremiumComponent},
    {path : 'sendMail' , component : SendMailComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
