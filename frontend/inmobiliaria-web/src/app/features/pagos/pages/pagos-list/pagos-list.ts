import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject } from '@angular/core';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';

import { PagoForm } from '../../components/pago-form/pago-form';
import { ConfirmDialog } from '../../../../shared/components/confirm-dialog/confirm-dialog';
import { MatTooltipModule } from '@angular/material/tooltip';

import { PageHeader } from '../../../../shared/components/page-header/page-header';

import { Snackbar } from '../../../../shared/services/snackbar';

import { Pago } from '../../models/pago.model';
import { PagoService } from '../../services/pago.service';

@Component({
  selector: 'app-pagos-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatDialogModule,
    MatTooltipModule,
    PageHeader
  ],
  templateUrl: './pagos-list.html',
  styleUrl: './pagos-list.scss'
})
export class PagosList {

  private readonly pagoService = inject(PagoService);
  private readonly snackbar = inject(Snackbar);
  private readonly dialog = inject(MatDialog);
  private readonly cdr = inject(ChangeDetectorRef);

  pagos: Pago[] = [];

  columnasMostradas = [
    'contrato',
    'cliente',
    'propiedad',
    'estado',
    'cuota',
    'fecha',
    'valor',
    'medioPago',
    'saldo',
    'acciones'
  ];

  ngOnInit(): void {
    this.cargarPagos();
  }

  cargarPagos(): void {

    this.pagoService
      .obtenerPagos()
      .subscribe({

        next: (respuesta) => {

          this.pagos = respuesta.contenido;
          this.cdr.detectChanges();

        },

        error: (error: any) => {

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible cargar los pagos.';

          this.snackbar.error(mensaje);

        }

      });

  }

  abrirFormulario(): void {

    const dialogRef = this.dialog.open(
      PagoForm,
      {
        width: '700px',
        disableClose: true
      }
    );

    dialogRef.afterClosed().subscribe(resultado => {

      if (resultado) {
        this.cargarPagos();
      }

    });

  }

  anularPago(pago: Pago): void {

    const dialogRef = this.dialog.open(
      ConfirmDialog,
      {
        width: '420px',
        disableClose: true,
        data: {
          titulo: 'Anular pago',
          mensaje:
            `¿Está seguro de anular el pago de la cuota ${pago.numeroCuota}?`,
          textoAceptar: 'Anular',
          textoCancelar: 'Cancelar'
        }
      }
    );

    dialogRef.afterClosed().subscribe(confirmado => {

      if (!confirmado) {
        return;
      }

      this.pagoService
        .anularPago(pago.idPago)
        .subscribe({

          next: () => {

            this.snackbar.exito(
              'Pago anulado correctamente.'
            );

            this.cargarPagos();

          },

          error: (error: any) => {

            const mensaje =
              error?.error?.mensaje ??
              'No fue posible anular el pago.';

            this.snackbar.error(mensaje);

          }

        });

    });

  }

}
