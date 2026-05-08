import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { OrderService } from '../../../../services/order.service';
import { OrderDetail } from '../../../../models/order.model';

@Component({
  selector: 'app-admin-order-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.css'
})
export class AdminOrderDetailComponent implements OnInit {
  order: OrderDetail | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';
  
  orderStatuses = ['Pending', 'Processing', 'Shipped', 'Delivered'];

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.fetchOrderDetails(+id);
    }
  }

  fetchOrderDetails(id: number) {
    this.isLoading = true;
    this.orderService.getOrderById(id).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.succeeded) {
          this.order = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to load order details.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'An error occurred while fetching order details.';
      }
    });
  }

  updateStatus() {
    if (!this.order) return;

    const currentIndex = this.orderStatuses.indexOf(this.order.status);
    if (currentIndex < this.orderStatuses.length - 1) {
      const nextIndex = currentIndex + 1;
      this.orderService.updateOrderStatus(this.order.id, nextIndex).subscribe({
        next: (response) => {
          if (response.succeeded) {
            this.order!.status = this.orderStatuses[nextIndex];
          }
        },
        error: (err) => console.error('Status update failed', err)
      });
    }
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
