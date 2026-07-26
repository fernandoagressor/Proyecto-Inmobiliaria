import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { EmpleadoForm } from '../../components/empleado-form/empleado-form';
import { EmpleadoService } from '../../services/empleado.service';
import { Empleado } from '../../models/empleado.model';

import {
  ConfirmDialog
} from '../../../../shared/components/confirm-dialog/confirm-dialog';

import { Snackbar } from '../../../../shared/services/snackbar';

@Component({
  selector: 'app-empleados-list',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    EmpleadoForm
  ],
  templateUrl: './empleados-list.html',
  styleUrl: './empleados-list.scss'
})
export class EmpleadosList implements OnInit {

  private readonly empleadoService =
    inject(EmpleadoService);

  private readonly dialog =
    inject(MatDialog);

  private readonly snackbar =
    inject(Snackbar);

  mostrarFormulario = false;

  empleadoSeleccionado: Empleado | null = null;

  empleados: Empleado[] = [];

  columnas: string[] = [
    'idEmpleado',
    'nombre',
    'documento',
    'telefono',
    'correo',
    'cargo',
    'fechaIngreso',
    'acciones'
  ];

  pagina = 0;
  tamano = 10;
  totalRegistros = 0;

  cargando = false;

  ngOnInit(): void {
    this.cargarEmpleados();
  }

  cargarEmpleados(): void {

    this.cargando = true;

    this.empleadoService.listar(
      this.pagina,
      this.tamano
    ).subscribe({

      next: (respuesta) => {

        this.empleados =
          respuesta.contenido;

        this.totalRegistros =
          respuesta.totalRegistros;

        this.cargando = false;
      },

      error: (error) => {

        console.error(
          'Error al cargar empleados:',
          error
        );

        this.snackbar.error(
          'No fue posible cargar los empleados'
        );

        this.cargando = false;
      }
    });
  }

  cambiarPagina(evento: PageEvent): void {

    this.pagina = evento.pageIndex;
    this.tamano = evento.pageSize;

    this.cargarEmpleados();
  }

  nuevoEmpleado(): void {

    this.empleadoSeleccionado = null;

    this.mostrarFormulario = true;
  }

  editarEmpleado(
    empleado: Empleado
  ): void {

    this.empleadoSeleccionado = empleado;

    this.mostrarFormulario = true;
  }

  eliminarEmpleado(
    empleado: Empleado
  ): void {

    const referencia =
      this.dialog.open(ConfirmDialog, {
        width: '420px',
        data: {
          titulo: 'Eliminar empleado',
          mensaje:
            `¿Seguro que deseas eliminar a ${empleado.nombres} ${empleado.apellidos}?`,
          textoAceptar: 'Eliminar',
          textoCancelar: 'Cancelar'
        }
      });

    referencia.afterClosed().subscribe(
      confirmado => {

        if (!confirmado) {
          return;
        }

        this.empleadoService
          .eliminar(empleado.idEmpleado)
          .subscribe({

            next: () => {

              this.snackbar.exito(
                'Empleado eliminado correctamente'
              );

              this.cargarEmpleados();
            },

            error: (error) => {

              console.error(
                'Error al eliminar empleado:',
                error
              );

              this.snackbar.error(
                'No fue posible eliminar el empleado'
              );
            }
          });
      }
    );
  }
  cerrarFormulario(): void {

    this.mostrarFormulario = false;

    this.empleadoSeleccionado = null;
  }

  empleadoGuardado(): void {

    this.cerrarFormulario();

    this.cargarEmpleados();
  }
}
