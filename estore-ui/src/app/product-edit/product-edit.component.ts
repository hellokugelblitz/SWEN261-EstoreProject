import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router'

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {
  product: Product | undefined;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private http: HttpClient,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getProduct();

  }

  getProduct(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.productService.getProduct(id)
      .subscribe(product => this.product = product);
  }

  goBack(): void {
    this.location.back();
  }

  //Used to redirect the user to the page of the new product.
  redirectTo(uri: string) {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() =>
      this.router.navigate([uri]));
  }

  //A semantic issue occurs here in because I want to be able to update the ID, but cannot,
  //because updateProduct() needs to pass in the Product object with an already existing ID.
  save(event: any): void {
    if (this.product) {
      this.product.name = event.target.Name.value;
      this.product.price = event.target.Price.value;
      this.product.id = this.product.id; //Nothing happens here.
      this.productService.updateProduct(this.product)
        .subscribe(() => this.goBack());
    }
  }

  delete(event: any): void {
    if (this.product) {
      this.productService.deleteProduct(this.product.id, true).subscribe(() => this.redirectTo("admin"));
      // this.productService.updateProduct(this.product)
      // .subscribe(() => this.goBack());
      this.goBack()
    }
  }

}
