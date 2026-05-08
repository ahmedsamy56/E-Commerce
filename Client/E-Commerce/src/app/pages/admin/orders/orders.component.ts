import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../../services/order.service';
import { Order } from '../../../models/order.model';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class AdminOrdersComponent implements OnInit {
  orders: Order[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.fetchOrders();
  }

  fetchOrders() {
    this.isLoading = true;
    this.orderService.getAllOrders().subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.succeeded) {
          this.orders = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to load orders.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'An error occurred while fetching orders.';
      }
    });
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'Pending': return 'bg-warning-subtle text-warning';
      case 'Processing': return 'bg-info-subtle text-info';
      case 'Shipped': return 'bg-primary-subtle text-primary';
      case 'Delivered': return 'bg-success-subtle text-success';
      default: return 'bg-light text-dark';
    }
  }
}
