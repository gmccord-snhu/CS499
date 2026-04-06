import {
  Component,
  OnInit,
  ChangeDetectorRef,
  Input,
  OnChanges,
  SimpleChanges,
  Output,
  EventEmitter
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-outcome-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './outcome-table.html',
  styleUrls: ['./outcome-table.css'],
})
export class OutcomeTableComponent implements OnInit, OnChanges {
  @Input() selectedRescueType = '';
  @Input() searchId = '';
  @Output() rowSelected = new EventEmitter<any>();
  @Output() animalsLoaded = new EventEmitter<any[]>();

  outcomes: any[] = [];
  isLoading = true;
  selectedRow: any = null;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    public auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadOutcomes();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedRescueType'] && !changes['selectedRescueType'].firstChange) {
      this.selectedRow = null;

      if (this.selectedRescueType) {
        this.loadFilteredOutcomes(this.selectedRescueType);
      } else {
        this.loadOutcomes();
      }
    }

    if (changes['searchId'] && !changes['searchId'].firstChange) {
      this.updateSelectedRowFromSearch();
    }
  }

  loadOutcomes(): void {
    this.isLoading = true;

    this.http.get<any[]>('http://localhost:3000/api/outcomes').subscribe({
      next: (data) => {
        this.outcomes = Array.isArray(data) ? data : [];
        this.animalsLoaded.emit(this.outcomes);
        this.updateSelectedRowFromSearch();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.outcomes = [];
        this.animalsLoaded.emit([]);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadFilteredOutcomes(type: string): void {
    this.isLoading = true;

    this.http.post<any[]>('http://localhost:3000/api/animals/filter', { type }).subscribe({
      next: (data) => {
        this.outcomes = Array.isArray(data) ? data : [];
        this.animalsLoaded.emit(this.outcomes);
        this.updateSelectedRowFromSearch();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.outcomes = [];
        this.animalsLoaded.emit([]);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  get filteredOutcomes(): any[] {
    if (!this.searchId || !this.searchId.trim()) {
      return this.outcomes;
    }

    const searchValue = this.searchId.trim();

    return this.outcomes.filter(item =>
      String(item._id || '') === searchValue ||
      String(item['Animal ID'] || '') === searchValue ||
      String(item.ID || '') === searchValue
    );
  }

  updateSelectedRowFromSearch(): void {
    if (!this.searchId || !this.searchId.trim()) {
      return;
    }

    const match = this.filteredOutcomes[0];
    if (match) {
      this.selectedRow = match;
      this.rowSelected.emit(match); 
    }
  }

  selectRow(animal: any): void {
    this.selectedRow = animal;
    this.rowSelected.emit(animal);
  }

  editOutcome(item: any): void {
    console.log('Edit button clicked');
    console.log('Item:', item);

    if (!item) {
      console.error('Item is undefined/null');
      return;
    }

    if (!item._id) {
      console.error('Missing Mongo _id:', item);
      return;
    }

    console.log('Navigating to:', `/outcomes/edit/${item._id}`);

    this.router.navigate(['/outcomes/edit', item._id])
      .then(success => {
        console.log('Navigation success:', success);
      })
      .catch(err => {
        console.error('Navigation error:', err);
      });
  }
}