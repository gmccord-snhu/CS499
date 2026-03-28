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

@Component({
  selector: 'app-outcome-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './outcome-table.html',
  styleUrls: ['./outcome-table.css'],
})
export class OutcomeTableComponent implements OnInit, OnChanges {
  @Input() selectedRescueType = '';
  @Output() rowSelected = new EventEmitter<any>();

  outcomes: any[] = [];
  isLoading = true;
  selectedRow: any = null;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
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
  }

  loadOutcomes(): void {
    this.isLoading = true;

    this.http.get<any[]>('http://localhost:3000/api/outcomes').subscribe({
      next: (data) => {
        this.outcomes = Array.isArray(data) ? data : [];
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.outcomes = [];
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
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.outcomes = [];
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  selectRow(animal: any): void {
    this.selectedRow = animal;
    this.rowSelected.emit(animal);
  }
}