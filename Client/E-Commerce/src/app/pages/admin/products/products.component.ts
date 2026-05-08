import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { CategoryService } from '../../../services/category.service';
import { Product } from '../../../models/product.model';
import { Category } from '../../../models/category.model';
import { ProductFormComponent } from './product-form/product-form.component';

@Component({
  selector: 'app-admin-products',
  standalone: true,
  imports: [CommonModule, FormsModule, ProductFormComponent],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class AdminProductsComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  selectedCategoryId: number | null = null;
  isLoading: boolean = false;
  errorMessage: string = '';

  // Modal control
  showModal: boolean = false;
  editingProduct: Product | null = null;

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

  onCategoryChange() {
    this.fetchProducts();
  }

  openAddModal() {
    this.editingProduct = null;
    this.showModal = true;
  }

  openEditModal(product: Product) {
    this.editingProduct = product;
    this.showModal = true;
  }

  handleSave(productData: any) {
    if (this.editingProduct) {
      this.productService.updateProduct(this.editingProduct.id, productData).subscribe({
        next: (response) => {
          if (response.succeeded) {
            this.showModal = false;
            this.fetchProducts();
          }
        },
        error: (err) => console.error('Update failed', err)
      });
    } else {
      this.productService.addProduct(productData).subscribe({
        next: (response) => {
          if (response.succeeded) {
            this.showModal = false;
            this.fetchProducts();
          }
        },
        error: (err) => console.error('Add failed', err)
      });
    }
  }

  deleteProduct(id: number) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: (response) => {
          if (response.succeeded) {
            this.fetchProducts();
          }
        },
        error: (err) => console.error('Delete failed', err)
      });
    }
  }
}
