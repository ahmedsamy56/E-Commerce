import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { OrderDetail } from '../../models/order.model';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent implements OnInit {
  order: OrderDetail | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';

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
        console.error(err);
        this.errorMessage =  err.error?.message || 'An error occurred while fetching order details.';
      }
    });
  }

  formatDate(dateArray: number[]): string {
    if (!dateArray || dateArray.length < 3) return 'N/A';
    const date = `${dateArray[0]}-${String(dateArray[1]).padStart(2, '0')}-${String(dateArray[2]).padStart(2, '0')}`;
    const time = dateArray.length >= 5 ? ` ${String(dateArray[3]).padStart(2, '0')}:${String(dateArray[4]).padStart(2, '0')}` : '';
    return date + time;
  }

  getStatusClass(status: string): string {
    if (!status) return 'bg-secondary';
    switch (status.toLowerCase()) {
      case 'pending': return 'bg-warning-subtle text-warning';
      case 'shipped': return 'bg-info-subtle text-info';
      case 'delivered': return 'bg-success-subtle text-success';
      case 'cancelled': return 'bg-danger-subtle text-danger';
      default: return 'bg-secondary-subtle text-secondary';
    }
  }
}
