import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  userName: string = '';
  cartItemCount: number = 0;
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;

  constructor(
    private userService: UserService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit() {
    this.checkLoginStatus();
    
    // Subscribe to cart changes
    this.cartService.cart$.subscribe(() => {
      this.cartItemCount = this.cartService.getItemCount();
    });

    // Update login status when user changes (optional: could use a service for this)
    const user = this.userService.getUser();
    if (user) {
      this.userName = user.name || user.username || 'User';
      this.isLoggedIn = true;
      this.isAdmin = this.userService.isAdmin();
    }
  }

  checkLoginStatus() {
    this.isLoggedIn = !!this.userService.getToken();
  }

  logout() {
    this.userService.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }
}
