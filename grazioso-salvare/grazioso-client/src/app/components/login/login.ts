import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html'
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.auth.login(this.username, this.password).subscribe({
      next: (user: any) => {
        this.auth.setAdminStatus(user?.role === 'admin');
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        this.error = 'Login failed';
      }
    });
  }
}