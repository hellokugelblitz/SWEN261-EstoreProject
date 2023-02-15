import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-card-creation',
  templateUrl: './card-creation.component.html',
  styleUrls: ['./card-creation.component.css']
})
export class CardCreationComponent implements OnInit {

  image!: File;
  name: string = "Test";
  price: number = 0;
  fileName: string | undefined;

  constructor(private productService: ProductService, private userService: UserService) {
  }

  create(event: any) {
    this.productService.createCustomProduct(event.target.Name.value, event.target.Price.value, this.image, this.userService.getCurrentUser()?.name!)
  }

  onUploadImage(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {

      this.image = file

    });

    reader.readAsDataURL(file);
  }

  ngOnInit() {
  }

}