import { Component } from '@angular/core';
import { HeaderComponent } from './components/header/header';
import { OutcomeTableComponent } from './components/outcome-table/outcome-table';
import { MapComponent } from './components/map/map';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, OutcomeTableComponent, MapComponent],
  templateUrl: './app.html'
})

export class AppComponent {
  selectedRescueType = '';
  selectedAnimal: any = null;

  onRescueTypeSelected(type: string): void {
    this.selectedRescueType = type;
    console.log('APP selected rescue type:', this.selectedRescueType);
  }

  onRowSelected(animal: any): void {
    this.selectedAnimal = animal;
    console.log('SELECTED ANIMAL:', animal);
  }

}