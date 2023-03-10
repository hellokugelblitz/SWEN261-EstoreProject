import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username = ""

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  loggedIn(): boolean {
    return this.userService.isLoggedIn();

  }

  logout() {
    this.userService.logout();
  }

  setUser() {
    this.userService.setUser(this.username);
  }
}
