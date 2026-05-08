import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { ReviewService } from '../../services/review.service';
import { UserService } from '../../services/user.service';
import { CartService } from '../../services/cart.service';
import { Product } from '../../models/product.model';
import { Review } from '../../models/review.model';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent implements OnInit {
  product: Product | null = null;
  reviews: Review[] = [];
  isLoading: boolean = false;
  isLoadingReviews: boolean = false;
  isSubmittingReview: boolean = false;
  errorMessage: string = '';
  reviewErrorMessage: string = '';
  reviewSuccessMessage: string = '';
  currentUserId: number | null = null;
  editingReviewId: number | null = null;
  selectedQuantity: number = 1;

  newReview = {
    rating: 5,
    comment: ''
  };

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private reviewService: ReviewService,
    private userService: UserService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.currentUserId = this.userService.getUserId();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.fetchProductDetails(+id);
      this.fetchReviews(+id);
    }
  }

  fetchProductDetails(id: number) {
    this.isLoading = true;
    this.productService.getProductById(id).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.succeeded) {
          this.product = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to load product details.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        console.error(err);
        this.errorMessage = 'An error occurred while fetching product details.';
      }
    });
  }

  fetchReviews(productId: number) {
    this.isLoadingReviews = true;
    this.reviewService.getReviewsByProductId(productId).subscribe({
      next: (response) => {
        this.isLoadingReviews = false;
        if (response.succeeded) {
          this.reviews = response.data;
        }
      },
      error: (err) => {
        this.isLoadingReviews = false;
        console.error('Failed to load reviews', err);
      }
    });
  }

  submitReview() {
    if (!this.product) return;

    this.reviewErrorMessage = '';
    this.reviewSuccessMessage = '';
    this.isSubmittingReview = true;

    const reviewData = {
      productId: this.product.id,
      rating: this.newReview.rating,
      comment: this.newReview.comment
    };

    const request = this.editingReviewId 
      ? this.reviewService.updateReview(this.editingReviewId, reviewData)
      : this.reviewService.addReview(reviewData);

    request.subscribe({
      next: (response) => {
        this.isSubmittingReview = false;
        if (response.succeeded) {
          this.reviewSuccessMessage = this.editingReviewId ? 'Review updated successfully!' : 'Review added successfully!';
          this.cancelEdit();
          this.fetchReviews(this.product!.id);
        } else {
          this.reviewErrorMessage = response.message || 'Failed to process review.';
        }
      },
      error: (err) => {
        this.isSubmittingReview = false;
        console.error(err);
        this.reviewErrorMessage = err.error?.message || 'An error occurred. Please try again.';
      }
    });
  }

  onEditReview(review: Review) {
    this.editingReviewId = review.id;
    this.newReview = {
      rating: review.rating,
      comment: review.comment
    };
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit() {
    this.editingReviewId = null;
    this.newReview = { rating: 5, comment: '' };
    this.reviewErrorMessage = '';
    this.reviewSuccessMessage = '';
  }

  deleteReview(reviewId: number) {
    if (!confirm('Are you sure you want to delete this review?')) return;

    this.reviewService.deleteReview(reviewId).subscribe({
      next: (response) => {
        if (response.succeeded) {
          this.fetchReviews(this.product!.id);
        }
      },
      error: (err) => {
        console.error(err);
        alert('Failed to delete review.');
      }
    });
  }

  addToCart() {
    if (this.product) {
      this.cartService.addToCart(this.product, this.selectedQuantity);
      alert(`${this.product.name} added to cart!`);
      this.selectedQuantity = 1;
    }
  }
}
