import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginData = {
    email: '',
    password: ''
  };

  errorMessage: string = '';
  successMessage: string = '';
  isLoading: boolean = false;

  constructor(private userService: UserService, private router: Router) {}

  onLogin() {
    this.errorMessage = '';
    this.successMessage = '';
    this.isLoading = true;

    this.userService.login(this.loginData).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.succeeded) {
          this.router.navigate(['/home']);
        } else {
          this.errorMessage = response.message || 'Login failed.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        console.error(err);
        this.errorMessage = err.error?.message || 'An error occurred during login. Please try again.';
      }
    });
  }
}
