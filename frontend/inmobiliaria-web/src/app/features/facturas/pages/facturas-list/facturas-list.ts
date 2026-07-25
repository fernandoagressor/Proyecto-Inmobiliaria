import { CommonModule } from '@angular/common';

import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import {
  ConfirmDialog,
  ConfirmDialogData
} from '../../../../shared/components/confirm-dialog/confirm-dialog';

import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';
import { Snackbar } from '../../../../shared/services/snackbar';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FacturaForm } from '../../components/factura-form/factura-form';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';

import { Factura } from '../../models/factura.model';
import { FacturaService } from '../../services/factura.service';

@Component({
  selector: 'app-facturas-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatDialogModule,
    FacturaForm
  ],
  templateUrl: './facturas-list.html',
  styleUrl: './facturas-list.scss'
})
export class FacturasList implements OnInit {

  private readonly dialog = inject(MatDialog);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly facturaService = inject(FacturaService);
  private readonly snackbar = inject(Snackbar);
  mostrarFormulario = false;
  facturaSeleccionada: Factura | null = null;
  facturas: Factura[] = [];

  columnas: string[] = [
    'numeroFactura',
    'idContrato',
    'fechaEmision',
    'fechaVencimiento',
    'valorFactura',
    'estado',
    'acciones'
  ];

  cargando = false;

  paginaActual = 0;
  tamanoPagina = 10;
  totalRegistros = 0;

  ngOnInit(): void {
    this.cargarFacturas();
  }

  cargarFacturas(): void {
    this.cargando = true;

    this.facturaService.listar(
      this.paginaActual,
      this.tamanoPagina,
      'idFactura',
      'asc'
    ).subscribe({
      next: (respuesta) => {
        this.facturas = respuesta.contenido;
        this.totalRegistros = respuesta.totalRegistros;
        this.cargando = false;

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error(
          'Error al cargar las facturas:',
          error
        );

        this.cargando = false;

        this.cdr.detectChanges();
      }
    });
  }

  cambiarPagina(event: PageEvent): void {
    this.paginaActual = event.pageIndex;
    this.tamanoPagina = event.pageSize;

    this.cargarFacturas();
  }

  nuevaFactura(): void {
    this.facturaSeleccionada = null;
    this.mostrarFormulario = true;
  }

  editar(factura: Factura): void {
    this.facturaSeleccionada = factura;
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.facturaSeleccionada = null;
  }

  facturaGuardada(): void {
    this.mostrarFormulario = false;
    this.facturaSeleccionada = null;
    this.paginaActual = 0;

    this.cargarFacturas();

    this.cdr.detectChanges();
  }

  eliminar(factura: Factura): void {

    const data: ConfirmDialogData = {
      titulo: 'Eliminar factura',
      mensaje: `¿Seguro que deseas eliminar la factura #${factura.numeroFactura}?`,
      textoAceptar: 'Eliminar',
      textoCancelar: 'Cancelar'
    };

    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '420px',
      data
    });

    dialogRef.afterClosed().subscribe((confirmado: boolean) => {

      if (!confirmado) {
        return;
      }

      this.facturaService
        .eliminar(factura.idFactura)
        .subscribe({

          next: () => {

            this.snackbar.exito(
              `Factura #${factura.numeroFactura} eliminada correctamente`
            );

            this.cargarFacturas();
          },

          error: (error) => {

            console.error(
              'Error al eliminar la factura:',
              error
            );

            const mensaje =
              error?.error?.mensaje ??
              'No fue posible eliminar la factura';

            this.snackbar.error(mensaje);

            this.cdr.detectChanges();
          }

        });

    });
  }
}
