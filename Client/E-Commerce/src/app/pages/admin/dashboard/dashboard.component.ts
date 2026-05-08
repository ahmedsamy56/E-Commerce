import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService, DashboardStats } from '../../../services/admin.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.fetchStats();
  }

  fetchStats() {
    this.isLoading = true;
    this.adminService.getDashboardStats().subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.succeeded) {
          this.stats = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to load stats.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        console.error(err);
        this.errorMessage = err.error?.message || 'An error occurred while fetching dashboard statistics.';
      }
    });
  }
}
