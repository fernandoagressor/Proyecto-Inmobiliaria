import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit
} from '@angular/core';

import { PropiedadForm } from '../../components/propiedad-form/propiedad-form';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import {
  MatDialog,
  MatDialogModule
} from '@angular/material/dialog';

import { PageHeader } from '../../../../shared/components/page-header/page-header';
import { ConfirmDialog } from '../../../../shared/components/confirm-dialog/confirm-dialog';
import { Snackbar } from '../../../../shared/services/snackbar';

import { Propiedad } from '../../models/propiedad.model';
import { PropiedadService } from '../../services/propiedad.service';

@Component({
  selector: 'app-propiedades-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatDialogModule,
    PageHeader,
    PropiedadForm
  ],
  templateUrl: './propiedades-list.html',
  styleUrl: './propiedades-list.scss'
})
export class PropiedadesList implements OnInit {

  private readonly propiedadService =
    inject(PropiedadService);

  private readonly dialog =
    inject(MatDialog);

  private readonly snackbar =
    inject(Snackbar);

  private readonly changeDetectorRef =
    inject(ChangeDetectorRef);

  propiedades: Propiedad[] = [];

  columnasMostradas: string[] = [
    'codigo',
    'titulo',
    'direccion',
    'valor',
    'estado',
    'acciones'
  ];

  ngOnInit(): void {
    this.cargarPropiedades();
  }

  cargarPropiedades(): void {

    this.propiedadService
      .obtenerPropiedades()
      .subscribe({

        next: (respuesta) => {

          this.propiedades =
            respuesta.contenido;

          this.changeDetectorRef
            .detectChanges();

        },

        error: (error) => {

          console.error(
            'Error al cargar propiedades:',
            error
          );

          const mensaje =
            error?.error?.mensaje ||
            'No fue posible cargar las propiedades.';

          this.snackbar.error(mensaje);

        }

      });

  }

  abrirFormulario(): void {

    const dialogRef = this.dialog.open(
      PropiedadForm,
      {
        width: '700px'
      }
    );

    dialogRef.afterClosed().subscribe(resultado => {

      if (resultado) {
        this.cargarPropiedades();
      }

    });

  }

  editarPropiedad(
    propiedad: Propiedad
  ): void {

    const dialogRef = this.dialog.open(
      PropiedadForm,
      {
        width: '700px',
        data: propiedad
      }
    );

    dialogRef.afterClosed().subscribe(resultado => {

      if (resultado) {
        this.cargarPropiedades();
      }

    });

  }

  eliminarPropiedad(
    propiedad: Propiedad
  ): void {

    const dialogRef =
      this.dialog.open(ConfirmDialog, {
        width: '420px',
        disableClose: true,
        data: {
          titulo: 'Eliminar propiedad',
          mensaje:
            `¿Está seguro de eliminar la propiedad ${propiedad.titulo}?`,
          textoAceptar: 'Eliminar',
          textoCancelar: 'Cancelar'
        }
      });

    dialogRef.afterClosed()
      .subscribe((confirmado) => {

        if (!confirmado) {
          return;
        }

        this.propiedadService
          .eliminarPropiedad(
            propiedad.idPropiedad
          )
          .subscribe({

            next: () => {

              this.snackbar.exito(
                'Propiedad eliminada correctamente.'
              );

              this.cargarPropiedades();

            },

            error: (error) => {

              console.error(
                'Error al eliminar la propiedad:',
                error
              );

              const mensaje =
                error?.error?.mensaje ||
                'No fue posible eliminar la propiedad.';

              this.snackbar.error(mensaje);

            }

          });

      });

  }

}
