import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit
} from '@angular/core';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';

import {
  MatDialog,
  MatDialogModule
} from '@angular/material/dialog';

import { Snackbar } from '../../../../shared/services/snackbar';
import { ClienteForm } from '../../components/cliente-form/cliente-form';
import { PageHeader } from '../../../../shared/components/page-header/page-header';
import { ConfirmDialog } from '../../../../shared/components/confirm-dialog/confirm-dialog';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-clientes-list',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    PageHeader,
    MatDialogModule
  ],
  templateUrl: './clientes-list.html',
  styleUrl: './clientes-list.scss'
})
export class ClientesList implements OnInit {

  private readonly clienteService = inject(ClienteService);
  private readonly dialog = inject(MatDialog);
  private readonly snackbar = inject(Snackbar);
  private readonly changeDetectorRef =
    inject(ChangeDetectorRef);



  clientes: Cliente[] = [];

  columnasMostradas: string[] = [
    'idCliente',
    'nombreCompleto',
    'telefono',
    'correo',
    'direccion',
    'acciones'
  ];

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {

    this.clienteService.obtenerClientes()
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Respuesta del backend:',
            respuesta
          );

          this.clientes = respuesta.contenido;

          this.changeDetectorRef.detectChanges();
        },

        error: (error) => {

          console.error(
            'Error al cargar clientes:',
            error
          );

        }

      });

  }

  abrirFormulario(): void {

    const dialogRef = this.dialog.open(ClienteForm, {
      width: '600px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe((resultado) => {

      if (resultado) {
        this.cargarClientes();
      }

    });

  }

  editarCliente(cliente: Cliente): void {

    const dialogRef = this.dialog.open(ClienteForm, {
      width: '600px',
      disableClose: true,
      data: cliente
    });

    dialogRef.afterClosed().subscribe((resultado) => {

      if (resultado) {
        this.cargarClientes();
      }

    });

  }

  eliminarCliente(cliente: Cliente): void {

    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '420px',
      disableClose: true,
      data: {
        titulo: 'Eliminar cliente',
        mensaje:
          `¿Está seguro de eliminar al cliente ${cliente.nombreCompleto}?`,
        textoAceptar: 'Eliminar',
        textoCancelar: 'Cancelar'
      }
    });

    dialogRef.afterClosed().subscribe((confirmado) => {

      if (!confirmado) {
        return;
      }

      this.clienteService
        .eliminarCliente(cliente.idCliente)
        .subscribe({

          next: () => {

            this.snackbar.exito(
              'Cliente eliminado correctamente.'
            );

            this.cargarClientes();

          },

          error: (error) => {

            console.error(
              'Error al eliminar el cliente:',
              error
            );

            const mensaje =
              error?.error?.mensaje ||
              'No fue posible eliminar el cliente.';

            this.snackbar.error(mensaje);

          }

        });

    });

  }

}
