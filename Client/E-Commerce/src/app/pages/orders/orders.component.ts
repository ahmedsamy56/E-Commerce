import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order.model';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, NavbarComponent, RouterLink],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {
  orders: Order[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.fetchMyOrders();
  }

  fetchMyOrders() {
    this.isLoading = true;
    this.orderService.getMyOrders().subscribe({
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
        console.error(err);
        this.errorMessage = 'An error occurred while fetching your orders.';
      }
    });
  }

  formatDate(dateArray: number[]): string {
    if (!dateArray || dateArray.length < 3) return 'N/A';
    // Format: YYYY-MM-DD HH:mm
    const date = `${dateArray[0]}-${String(dateArray[1]).padStart(2, '0')}-${String(dateArray[2]).padStart(2, '0')}`;
    const time = dateArray.length >= 5 ? ` ${String(dateArray[3]).padStart(2, '0')}:${String(dateArray[4]).padStart(2, '0')}` : '';
    return date + time;
  }

  getStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'pending': return 'bg-warning-subtle text-warning';
      case 'shipped': return 'bg-info-subtle text-info';
      case 'delivered': return 'bg-success-subtle text-success';
      default: return 'bg-secondary-subtle text-secondary';
    }
  }
}
