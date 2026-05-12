import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../../services/category.service';
import { Category } from '../../../models/category.model';
import { CategoryFormComponent } from './category-form/category-form.component';

@Component({
  selector: 'app-admin-categories',
  standalone: true,
  imports: [CommonModule, CategoryFormComponent],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class AdminCategoriesComponent implements OnInit {
  categories: Category[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  // Modal control
  showModal: boolean = false;
  editingCategory: Category | null = null;
  formError: string | null = null;

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.fetchCategories();
  }

  fetchCategories() {
    this.isLoading = true;
    this.categoryService.getCategories().subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.succeeded) {
          this.categories = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to load categories.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        console.error(err);
        this.errorMessage = err.error.message || 'An error occurred while fetching categories.';
      }
    });
  }

  openAddModal() {
    this.editingCategory = null;
    this.formError = null;
    this.showModal = true;
  }

  openEditModal(category: Category) {
    this.editingCategory = category;
    this.formError = null;
    this.showModal = true;
  }

  handleSave(categoryData: any) {
    this.formError = null;
    if (this.editingCategory) {
      this.categoryService.updateCategory(this.editingCategory.id, categoryData).subscribe({
        next: (response) => {
          if (response.succeeded) {
            this.showModal = false;
            this.fetchCategories();
          } else {
            this.formError = response.message || 'Failed to update category.';
          }
        },
        error: (err) => {
          console.error('Update failed', err);
          this.formError = err.error?.message || 'An error occurred during update.';
        }
      });
    } else {
      this.categoryService.addCategory(categoryData).subscribe({
        next: (response) => {
          if (response.succeeded) {
            this.showModal = false;
            this.fetchCategories();
          } else {
            this.formError = response.message || 'Failed to create category.';
          }
        },
        error: (err) => {
          console.error('Add failed', err);
          this.formError = err.error?.message || 'An error occurred during creation.';
        }
      });
    }
  }

  deleteCategory(id: number) {
    if (confirm('Are you sure you want to delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: (response) => {
          if (response.succeeded) {
            this.fetchCategories();
          }
        },
        error: (err) => console.error('Delete failed', err)
      });
    }
  }
}
