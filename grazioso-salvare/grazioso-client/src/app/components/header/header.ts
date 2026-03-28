import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class HeaderComponent {
  @Output() rescueTypeSelected = new EventEmitter<string>();

  onRescueTypeChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.rescueTypeSelected.emit(select.value);
  }
}