import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';
import { ProductCardComponent } from '../../components/product-card/product-card.component';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ProductCardComponent, NavbarComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  selectedCategoryId: number | null = null;
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService
  ) {}

  ngOnInit() {
    this.fetchCategories();
    this.fetchProducts();
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
}
