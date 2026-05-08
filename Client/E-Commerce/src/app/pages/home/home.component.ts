import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';
import { ProductCardComponent } from '../../components/product-card/product-card.component';
import { UserService } from '../../services/user.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, ProductCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  selectedCategoryId: number | null = null;
  errorMessage: string = '';
  isLoading: boolean = false;
  userName: string = '';
  cartItemCount: number = 0;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private userService: UserService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    const user = this.userService.getUser();
    if (user) {
      this.userName = user.name;
    }
    this.fetchCategories();
    this.fetchProducts();

    this.cartService.cart$.subscribe(() => {
      this.cartItemCount = this.cartService.getItemCount();
    });
  }

  fetchCategories() {
    this.categoryService.getCategories().subscribe({
      next: (response) => {
        if (response.succeeded) {
          this.categories = response.data;
        }
      },
      error: (err) => console.error('Failed to load categories', err)
    });
  }

  fetchProducts() {
    this.isLoading = true;
    this.productService.getProducts(this.selectedCategoryId || undefined).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.succeeded) {
          this.products = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to load products.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        console.error(err);
        this.errorMessage = 'An error occurred while fetching products.';
      }
    });
  }

  onCategorySelect(categoryId: number | null) {
    this.selectedCategoryId = categoryId;
    this.fetchProducts();
  }

  logout() {
    this.userService.logout();
  }
}
