import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  products: Product[] = [];

  constructor(private productService: ProductService, 
              private userService: UserService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(1, 5));
  }

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