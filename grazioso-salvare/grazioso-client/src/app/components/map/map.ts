import {
  Component,
  Input,
  OnChanges,
  AfterViewInit,
  SimpleChanges
} from '@angular/core';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './map.html',
  styleUrls: ['./map.css']
})

export class MapComponent implements AfterViewInit, OnChanges {
  @Input() selectedAnimal: any = null;

  private map!: L.Map;
  private marker: L.Marker | null = null;

  ngAfterViewInit(): void {
    this.map = L.map('map', {
      center: [30.2672, -97.7431],
      zoom: 10
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedAnimal'] && this.map && this.selectedAnimal) {
      this.showAnimalOnMap();
    }
  }

  private showAnimalOnMap(): void {
    const lat = this.selectedAnimal.location_lat;
    const lng = this.selectedAnimal.location_long;

    if (typeof lat !== 'number' || typeof lng !== 'number') {
      return;
    }

    if (this.marker) {
      this.map.removeLayer(this.marker);
    }

    this.marker = L.marker([lat, lng]).addTo(this.map);

    this.marker.bindPopup(`
      <strong>${this.selectedAnimal.Name || 'Unknown'}</strong><br>
      Breed: ${this.selectedAnimal['Primary Breed'] || 'N/A'}<br>
      Type: ${this.selectedAnimal.Type || 'N/A'}
    `).openPopup();

    this.map.setView([lat, lng], 12);
  }
}