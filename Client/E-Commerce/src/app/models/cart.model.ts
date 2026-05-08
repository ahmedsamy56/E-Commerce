import { Product } from './product.model';

export interface CartItem {
    product: Product;
    quantity: number;
}

export interface ShippingInfo {
    address: string;
    city: string;
    state: string;
    country: string;
    zip_code: string;
}

export interface OrderRequest {
    items: { product_id: number; quantity: number }[];
    shipping: ShippingInfo;
}
