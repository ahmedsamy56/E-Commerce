import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Category } from '../../../../models/category.model';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './category-form.component.html',
  styleUrl: './category-form.component.css'
})
export class CategoryFormComponent implements OnInit {
  @Input() category: Category | null = null;
  @Input() serverError: string | null = null;
  @Output() save = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  categoryForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.categoryForm = this.fb.group({
      name: ['', Validators.required]
    });

    this.categoryForm.get('name')?.valueChanges.subscribe(() => {
      if (this.serverError) {
        this.serverError = null;
      }
    });
  }

  ngOnInit() {
    if (this.category) {
      this.categoryForm.patchValue(this.category);
    }
  }

  onSubmit() {
    if (this.categoryForm.valid) {
      this.save.emit(this.categoryForm.value);
    }
  }
}
