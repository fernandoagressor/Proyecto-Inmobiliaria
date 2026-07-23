import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { KpiCard } from '../../../../shared/components/kpi-card/kpi-card';
import { DashboardService } from '../../services/dashboard.service';
import { Dashboard as DashboardModel } from '../../models/dashboard.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    KpiCard
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {

  private readonly dashboardService = inject(DashboardService);

  datosDashboard: DashboardModel | null = null;
  cargando = true;
  error = '';

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard(): void {
    this.cargando = true;
    this.error = '';

    this.dashboardService.obtenerDashboard().subscribe({
      next: (respuesta) => {
        this.datosDashboard = respuesta;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar el dashboard:', error);
        this.error = 'No fue posible cargar la información del dashboard.';
        this.cargando = false;
      }
    });
  }
}
