import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../product';
import { UserService } from '../user.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  constructor(private userService: UserService, private router: Router) { }
  price=0;

  ngOnInit(): void {
    if (this.userService.isLoggedIn() == false) {
      this.router.navigate(['/login'])
    }
  }

  getCart(): Product[] {
    let cart = this.userService.getCurrentUser()?.shoppingCart;
    if (cart != undefined) {
      return cart
    }
    return []
  }

  getCartLen(): number {
    let cart = this.userService.getCurrentUser()?.shoppingCart;
    if (cart != undefined) {
      return cart.length
    }
    return 0
  }

  getPrice(): void {
    let products=this.getCart();
    for(let i=0;products[i]!=undefined; i++){
      this.price+=products[i].price;
    }
  }

  removeProduct(product: Product) {
    this.userService.removeFromShoppingCart(product);
  }

  checkout() {
    for (let product of this.getCart()) {
      this.userService.removeFromShoppingCart(product);
    }
  }

}
