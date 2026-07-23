import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { ContratoForm } from '../../components/contrato-form/contrato-form';
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

import { ContratoService } from '../../services/contrato.service';
import { Contrato } from '../../models/contrato.model';

@Component({
  selector: 'app-contratos-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatDialogModule,
    PageHeader,
    ContratoForm
  ],
  templateUrl: './contratos-list.html',
  styleUrl: './contratos-list.scss'
})
export class ContratosList implements OnInit {

  private readonly contratoService =
    inject(ContratoService);

  private readonly dialog =
    inject(MatDialog);

  private readonly snackbar =
    inject(Snackbar);

  private readonly changeDetectorRef =
    inject(ChangeDetectorRef);

  contratos: Contrato[] = [];

  columnasMostradas = [
    'cliente',
    'propiedad',
    'valorTotal',
    'cuotaInicial',
    'estado',
    'acciones'
  ];

  ngOnInit(): void {
    this.cargarContratos();
  }

  cargarContratos(): void {

    this.contratoService
      .obtenerContratos()
      .subscribe({

        next: (respuesta) => {

          this.contratos =
            respuesta.contenido;

          this.changeDetectorRef.detectChanges();

        },

        error: (error) => {

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible cargar los contratos.';

          this.snackbar.error(mensaje);

        }

      });

  }

  abrirFormulario(): void {

    const dialogRef = this.dialog.open(
      ContratoForm,
      {
        width: '850px'
      }
    );

    dialogRef.afterClosed().subscribe(resultado => {

      if (resultado) {
        this.cargarContratos();
      }

    });

  }

  editarContrato(
    contrato: Contrato
  ): void {

    const dialogRef = this.dialog.open(
      ContratoForm,
      {
        width: '850px',
        data: contrato
      }
    );

    dialogRef.afterClosed().subscribe(resultado => {

      if (resultado) {
        this.cargarContratos();
      }

    });

  }

  eliminarContrato(
    contrato: Contrato
  ): void {

    const dialogRef = this.dialog.open(
      ConfirmDialog,
      {
        width: '420px',
        disableClose: true,
        data: {
          titulo: 'Eliminar contrato',
          mensaje: `¿Está seguro de eliminar el contrato de ${contrato.nombreCliente}?`,
          textoAceptar: 'Eliminar',
          textoCancelar: 'Cancelar'
        }
      }
    );

    dialogRef.afterClosed().subscribe(confirmado => {

      if (!confirmado) {
        return;
      }

      this.contratoService
        .eliminarContrato(contrato.idContrato)
        .subscribe({

          next: () => {

            this.snackbar.exito(
              'Contrato eliminado correctamente.'
            );

            this.cargarContratos();

          },

          error: (error) => {

            const mensaje =
              error?.error?.mensaje ??
              'No fue posible eliminar el contrato.';

            this.snackbar.error(mensaje);

          }

        });

    });

  }

}
