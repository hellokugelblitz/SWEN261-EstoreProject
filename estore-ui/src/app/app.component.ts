import { Component } from '@angular/core';
import { UserService } from './user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Pokemon Card Shop';

  constructor(private userService: UserService) { }

  loggedIn(): boolean {
    return this.userService.isLoggedIn()
  }

  //Determines wether the current user is an admin and is logged in.
  loggedInAsAdmin(): boolean {
    if (this.userService.isLoggedIn()) {
      if (this.userService.getCurrentUser()?.username == "admin") { return true; }
    }
    return false;
  }
}