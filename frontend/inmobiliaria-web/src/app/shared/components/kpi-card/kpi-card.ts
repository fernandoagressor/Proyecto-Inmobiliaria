import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-kpi-card',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule
  ],
  templateUrl: './kpi-card.html',
  styleUrl: './kpi-card.scss'
})
export class KpiCard {

  @Input({ required: true })
  titulo = '';

  @Input({ required: true })
  valor: string | number = 0;

  @Input({ required: true })
  icono = '';

  @Input()
  subtitulo = '';

  @Input()
  tipo: 'primary' | 'success' | 'warning' | 'danger' = 'primary';
}
