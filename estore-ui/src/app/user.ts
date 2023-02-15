import { Product } from "./product";

export interface User {
    username: string,
    shoppingCart: Product[],
    name: string
}