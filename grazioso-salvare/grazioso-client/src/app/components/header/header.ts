import { Component, EventEmitter, Output, Input, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class HeaderComponent {
  @Input() animals: any[] = [];

  @Output() rescueTypeSelected = new EventEmitter<string>();
  @Output() animalSelected = new EventEmitter<any>();
  @Output() searchId = new EventEmitter<string>();

  username = '';
  password = '';
  loginError = '';

  animalIdSearch = '';
  searchError = '';

  constructor(
    public auth: AuthService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  onRescueTypeChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.rescueTypeSelected.emit(select.value);
  }

  findAnimalById(): void {
    const searchId = this.animalIdSearch.trim();

    if (!searchId) {
      return;
    }

    this.searchError = '';
    this.searchId.emit(searchId);
  }

  onSearchInputChange(): void {
    this.searchError = '';

    if (!this.animalIdSearch || !this.animalIdSearch.trim()) {
      this.searchId.emit('');
    }
  }

  login(): void {
    console.log('login clicked');
    console.log('username:', this.username);
    console.log('password:', this.password);

    this.auth.login(this.username, this.password).subscribe({
      next: (_res: any) => {
        console.log('login success', _res);
        this.auth.isAdmin = true;
        this.loginError = '';
        this.password = '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('login failed', err);
        this.auth.isAdmin = false;
        this.loginError = 'Invalid username or password';
        alert(this.loginError);
        this.cdr.detectChanges();
      }
    });
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}