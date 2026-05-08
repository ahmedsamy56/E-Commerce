import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { CartItem, ShippingInfo, OrderRequest } from '../../models/cart.model';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterModule, FormsModule, NavbarComponent],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  isProcessing: boolean = false;
  showCheckout: boolean = false;

  shippingInfo: ShippingInfo = {
    address: '',
    city: '',
    state: '',
    country: 'Egypt',
    zip_code: ''
  };

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cartService.cart$.subscribe(items => {
      this.cartItems = items;
      this.calculateTotal();
    });
  }

  calculateTotal() {
    this.totalPrice = this.cartService.getTotalPrice();
  }

  updateQuantity(productId: number, quantity: number) {
    this.cartService.updateQuantity(productId, quantity);
  }

  removeItem(productId: number) {
    this.cartService.removeFromCart(productId);
  }

  placeOrder() {
    if (this.cartItems.length === 0) return;

    this.isProcessing = true;
    const order: OrderRequest = {
      items: this.cartItems.map(item => ({
        product_id: item.product.id,
        quantity: item.quantity
      })),
      shipping: this.shippingInfo
    };

    this.orderService.placeOrder(order).subscribe({
      next: (response) => {
        this.isProcessing = false;
        if (response.succeeded) {
          alert('Order placed successfully!');
          this.cartService.clearCart();
          this.router.navigate(['/home']);
        } else {
          alert(response.message || 'Failed to place order.');
        }
      },
      error: (err) => {
        this.isProcessing = false;
        console.error(err);
        alert('An error occurred while placing your order.');
      }
    });
  }
}
