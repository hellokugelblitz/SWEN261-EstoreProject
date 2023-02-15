import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { ProductService } from '../product.service';
import { Product } from '../product';
import { ProductComponent } from '../products/products.component';
import { Router } from '@angular/router'



@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  products: Product[] = [];

  constructor(private productService: ProductService,
    private router: Router,
    private userService: UserService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(0, products.length));
  }

  //Used to redirect the user to the page of the new product.
  redirectTo(uri: string) {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() =>
      this.router.navigate([uri]));
  }

  //Determines wether the current user is an admin and is logged in.
  loggedInAsAdmin(): boolean {
    if (this.userService.isLoggedIn()) {
      if (this.userService.getCurrentUser()?.username == "admin") { return true; }
    }
    return false;
  }

  //Used to easily create a product object
  createProduct(name: string, price: number): Product {
    const newProduct = {} as Product;
    newProduct.name = name;
    newProduct.price = price;
    return newProduct;
  }

  //Used to create a new product
  newProduct(): void {
    this.productService.getNextId().subscribe(id => {
      this.productService.addProduct(this.createProduct("Product_" + id, 0))
        .subscribe(product => {
          this.products.push(product);
        });
    })
  }

}
