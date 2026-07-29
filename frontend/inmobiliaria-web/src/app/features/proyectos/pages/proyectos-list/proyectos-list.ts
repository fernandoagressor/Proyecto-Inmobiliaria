import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { ProjectTable } from '../../components/project-table/project-table';
import { PageHeader } from '../../../../shared/components/page-header/page-header';

import { Proyecto } from '../../models/proyecto';

@Component({
  selector: 'app-proyectos-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    PageHeader,
    ProjectTable
  ],
  templateUrl: './proyectos-list.html',
  styleUrl: './proyectos-list.scss'
})
export class ProyectosListComponent {

  columnasMostradas: string[] = [
    'codigo',
    'nombre',
    'estado',
    'fechaCreacion',
    'acciones'
  ];

  proyectos: Proyecto[] = [
    {
      id: 1,
      codigo: 'PRJ-001',
      nombre: 'Condominio Los Robles',
      descripcion: 'Proyecto residencial',
      estado: 'ACTIVO',
      fechaCreacion: new Date('2026-01-15')
    },
    {
      id: 2,
      codigo: 'PRJ-002',
      nombre: 'Torres del Lago',
      descripcion: 'Proyecto urbano',
      estado: 'ACTIVO',
      fechaCreacion: new Date('2026-03-08')
    }
  ];

  abrirFormulario(): void {
    console.log('Nuevo proyecto');
  }

  editarProyecto(proyecto: Proyecto): void {
    console.log('Editar proyecto', proyecto);
  }

  eliminarProyecto(proyecto: Proyecto): void {
    console.log('Eliminar proyecto', proyecto);
  }

}
