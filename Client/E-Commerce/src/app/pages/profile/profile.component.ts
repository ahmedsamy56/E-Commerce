import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { ReviewService } from '../../services/review.service';
import { Review } from '../../models/review.model';
import { AuthResponse } from '../../models/auth-response.model';
import { ReviewItemComponent } from '../../components/review-item/review-item.component';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReviewItemComponent, FormsModule, NavbarComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  user: AuthResponse | null = null;
  reviews: Review[] = [];
  isLoadingReviews: boolean = false;
  
  editingReview: Review | null = null;
  editModel = {
    rating: 5,
    comment: ''
  };
  isUpdating: boolean = false;
  isDeletingAccount: boolean = false;
  updateMessage: string = '';

  constructor(
    private userService: UserService,
    private reviewService: ReviewService,
    private router: Router
  ) {}

  ngOnInit() {
    this.user = this.userService.getUser();
    console.log('User object:', this.user);
    if (this.user) {
        console.log('Name property:', this.user.name);
        console.log('Username property:', this.user.username);
        console.log('All keys:', Object.keys(this.user));
    }
    this.fetchUserReviews();
  }

  deleteAccount() {
    if (!confirm('Are you sure you want to delete your account? This action cannot be undone.')) return;

    this.isDeletingAccount = true;
    this.userService.deleteAccount().subscribe({
      next: (response) => {
        this.isDeletingAccount = false;
        if (response.succeeded) {
          alert('Account deleted successfully.');
          this.userService.logout();
          this.router.navigate(['/login']);
        } else {
          alert(response.message || 'Failed to delete account.');
        }
      },
      error: (err) => {
        this.isDeletingAccount = false;
        console.error(err);
        alert('An error occurred while deleting your account.');
      }
    });
  }

  fetchUserReviews() {
    this.isLoadingReviews = true;
    this.reviewService.getUserReviews().subscribe({
      next: (response) => {
        this.isLoadingReviews = false;
        if (response.succeeded) {
          this.reviews = response.data;
        }
      },
      error: (err) => {
        this.isLoadingReviews = false;
        console.error('Failed to load user reviews', err);
      }
    });
  }

  onEditReview(review: Review) {
    this.editingReview = review;
    this.editModel = {
      rating: review.rating,
      comment: review.comment
    };
    // Scroll to edit form if it exists or show modal
    setTimeout(() => {
        const element = document.getElementById('edit-review-form');
        element?.scrollIntoView({ behavior: 'smooth' });
    }, 100);
  }

  cancelEdit() {
    this.editingReview = null;
    this.updateMessage = '';
  }

  updateReview() {
    if (!this.editingReview) return;

    this.isUpdating = true;
    this.updateMessage = '';

    const updateData = {
      productId: this.editingReview.productId,
      rating: this.editModel.rating,
      comment: this.editModel.comment
    };

    this.reviewService.updateReview(this.editingReview.id, updateData).subscribe({
      next: (response) => {
        this.isUpdating = false;
        if (response.succeeded) {
          this.updateMessage = 'Review updated successfully!';
          setTimeout(() => {
            this.cancelEdit();
            this.fetchUserReviews();
          }, 1500);
        } else {
          this.updateMessage = response.message || 'Failed to update review.';
        }
      },
      error: (err) => {
        this.isUpdating = false;
        this.updateMessage = 'An error occurred while updating.';
        console.error(err);
      }
    });
  }

  deleteReview(reviewId: number) {
    if (!confirm('Are you sure you want to delete this review?')) return;

    this.reviewService.deleteReview(reviewId).subscribe({
      next: (response) => {
        if (response.succeeded) {
          this.fetchUserReviews();
        }
      },
      error: (err) => {
        console.error(err);
        alert('Failed to delete review.');
      }
    });
  }
}
