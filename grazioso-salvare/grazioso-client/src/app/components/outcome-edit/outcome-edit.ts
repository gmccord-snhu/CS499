import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-outcome-edit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './outcome-edit.html',
  styleUrls: ['./outcome-edit.css']
})
export class OutcomeEditComponent implements OnInit {
  outcome: any = null;
  id: string = '';
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.id = params.get('id') || '';
      console.log('Route id:', this.id);

      if (!this.id) {
        console.error('No ID in route');
        this.isLoading = false;
        this.cdr.detectChanges();
        return;
      }

      this.isLoading = true;
      this.outcome = null;
      this.cdr.detectChanges();

      this.http.get(`http://localhost:3000/api/outcomes/${this.id}`, {
        withCredentials: true
      }).subscribe({
        next: (data) => {
          console.log('DATA RETURNED:', data);
          this.outcome = data;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error loading outcome:', err);
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    });
  }

  updateOutcome(): void {
    this.http.put(`http://localhost:3000/api/outcomes/${this.id}`, this.outcome, {
      withCredentials: true
    }).subscribe({
      next: () => {
        alert('Updated successfully');
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Update failed:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}