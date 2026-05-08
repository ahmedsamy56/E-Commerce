import { Product } from './product.model';

export interface Order {
  id: number;
  date: number[];
  status: string;
  user_name: string;
  total_price: number;
}

export interface OrderItemDetail {
  id: number;
  quantity: number;
  price: number;
  product: Product;
  product_id: number;
}

export interface ShippingInfo {
  id: number;
  orderId: number;
  date: number[];
  address: string;
  city: string;
  state: string;
  country: string;
  zipCode: string;
}

export interface OrderDetail {
  id: number;
  date: number[];
  status: string;
  items: OrderItemDetail[];
  shipping: ShippingInfo;
  total_price: number;
}
