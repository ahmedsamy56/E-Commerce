import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Review } from '../../models/review.model';

@Component({
  selector: 'app-review-item',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './review-item.component.html',
  styleUrl: './review-item.component.css'
})
export class ReviewItemComponent {
  @Input({ required: true }) review!: Review;
  @Input() currentUserId: number | null = null;
  @Input() showProductLink: boolean = false;

  @Output() edit = new EventEmitter<Review>();
  @Output() delete = new EventEmitter<number>();

  onEdit() {
    this.edit.emit(this.review);
  }

  onDelete() {
    this.delete.emit(this.review.id);
  }

  formatDate(dateArray: number[]): string {
    if (!dateArray || dateArray.length < 3) return 'Just now';
    return `${dateArray[0]}-${dateArray[1]}-${dateArray[2]}`;
  }
}
