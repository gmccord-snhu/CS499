import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header';
import { OutcomeTableComponent } from '../outcome-table/outcome-table';
import { MapComponent } from '../map/map';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, HeaderComponent, OutcomeTableComponent, MapComponent],
  templateUrl: './dashboard.html'
})
export class DashboardComponent implements OnInit {
  selectedRescueType = '';
  selectedAnimal: any = null;
  animals: any[] = [];
  searchId = '';

  constructor(public auth: AuthService) {}

  ngOnInit(): void {
  }

  onRescueTypeSelected(type: string): void {
    this.selectedRescueType = type;
  }

  onRowSelected(animal: any): void {
    this.selectedAnimal = animal;
  }

  onAnimalsLoaded(animals: any[]): void {
    this.animals = animals;
  }

  onSearchId(id: string): void {
    this.searchId = id;
  }
}