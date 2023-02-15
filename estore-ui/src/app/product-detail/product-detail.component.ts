import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';
import { User } from '../user';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
    selector: 'app-product-detail',
    templateUrl: './product-detail.component.html',
    styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
    product: Product | undefined;
    user: User | undefined;

    constructor(
        private route: ActivatedRoute,
        private productService: ProductService,
        private userService: UserService,
        private location: Location,
        private sanitizer: DomSanitizer
    ) { }

    ngOnInit(): void {
        this.getUser();
        this.getProduct();
    }

    getProduct(): void {
        const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
        if (this.user && this.user.shoppingCart != null && this.user?.shoppingCart.find(e => e.id == id) !== undefined) {
            let product = this.user?.shoppingCart.find(e => e.id == id)
            if (product?.custom) {
                this.productService.getCustomProduct(this.user.username, id)
                    .subscribe(product => this.product = product);
            } else { // duplicated code but oh well
                this.productService.getProduct(id)
                    .subscribe(product => this.product = product);
            }
        } else {
            this.productService.getProduct(id)
                .subscribe(product => this.product = product);
        }
    }

    getUser(): boolean {
        this.user = this.userService.getCurrentUser();
        return this.user != undefined;
    }

    goBack(): void {
        this.location.back();
    }

    inCart(): boolean {
        if (this.user && this.product && this.user.shoppingCart != null && this.user.shoppingCart.find(e => e.id == this.product?.id) !== undefined) {
            return true;
        } else {
            return false;
        }
    }

    getSanitizeUrl(name: string) {
        let url = `localhost:8080/products/image/${name}`
        let sanUrl = this.sanitizer.bypassSecurityTrustUrl(url);

        console.log(sanUrl)
        return sanUrl
    }

    addToCart(): void {
        this.userService.addToShoppingCart(this.product!)
        this.getUser()
    }

    removeFromCart(): void {
        this.userService.removeFromShoppingCart(this.product!)
        this.getUser()
    }

    save(): void {
        if (this.product) {
            this.productService.updateProduct(this.product)
                .subscribe(() => this.goBack());
        }
    }
}