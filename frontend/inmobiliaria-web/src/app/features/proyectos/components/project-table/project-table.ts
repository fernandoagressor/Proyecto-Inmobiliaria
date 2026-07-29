import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';

import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { Proyecto } from '../../models/proyecto';

@Component({
  selector: 'app-project-table',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './project-table.html',
  styleUrl: './project-table.scss'
})
export class ProjectTable {

  @Input({ required: true })
  proyectos: Proyecto[] = [];

  @Input({ required: true })
  columnasMostradas: string[] = [];

  @Output()
  editar = new EventEmitter<Proyecto>();

  @Output()
  eliminar = new EventEmitter<Proyecto>();

  editarProyecto(proyecto: Proyecto): void {
    this.editar.emit(proyecto);
  }

  eliminarProyecto(proyecto: Proyecto): void {
    this.eliminar.emit(proyecto);
  }

}
