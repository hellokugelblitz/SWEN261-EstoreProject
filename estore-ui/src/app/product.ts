export interface Product {
  id: number;
  name: string;
  price: number;
  image: File | null;
  custom: boolean;
}