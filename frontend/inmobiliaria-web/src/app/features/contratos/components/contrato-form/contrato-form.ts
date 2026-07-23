import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef
} from '@angular/material/dialog';

import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { Cliente } from '../../../clientes/models/cliente.model';
import { ClienteService } from '../../../clientes/services/cliente.service';

import { Propiedad } from '../../../propiedades/models/propiedad.model';
import { PropiedadService } from '../../../propiedades/services/propiedad.service';

import { Snackbar } from '../../../../shared/services/snackbar';

import { Contrato } from '../../models/contrato.model';
import { ContratoRequest } from '../../models/contrato-request.model';
import { ContratoService } from '../../services/contrato.service';

@Component({
  selector: 'app-contrato-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './contrato-form.html',
  styleUrl: './contrato-form.scss'
})
export class ContratoForm {

  private readonly fb = inject(FormBuilder);

  private readonly contratoService =
    inject(ContratoService);

  private readonly clienteService =
    inject(ClienteService);

  private readonly propiedadService =
    inject(PropiedadService);

  private readonly snackbar =
    inject(Snackbar);

  readonly contratoEditar =
    inject<Contrato | null>(
      MAT_DIALOG_DATA,
      { optional: true }
    );

  readonly dialogRef =
    inject(MatDialogRef<ContratoForm>);

  clientes: Cliente[] = [];
  propiedades: Propiedad[] = [];

  readonly modoEdicion =
    this.contratoEditar !== null;

  formulario = this.fb.group({

    idCliente: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    idPropiedad: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    valorTotal: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    cuotaInicial: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],

    numeroCuotas: [
      1,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    fechaInicio: [
      '',
      Validators.required
    ],

    estado: [
      '',
      Validators.required
    ]

  });

  constructor() {

    this.cargarClientes();
    this.cargarPropiedades();

    if (!this.contratoEditar) {
      return;
    }

    this.formulario.patchValue({

      idCliente:
        this.contratoEditar.idCliente,

      idPropiedad:
        this.contratoEditar.idPropiedad,

      valorTotal:
        this.contratoEditar.valorTotal,

      cuotaInicial:
        this.contratoEditar.cuotaInicial,

      numeroCuotas:
        this.contratoEditar.numeroCuotas,

      fechaInicio:
        this.contratoEditar.fechaInicio,

      estado:
        this.contratoEditar.estado

    });

  }

  private cargarClientes(): void {

    this.clienteService
      .obtenerClientes()
      .subscribe({

        next: (respuesta) => {

          this.clientes =
            respuesta.contenido;

        },

        error: () => {

          this.snackbar.error(
            'No fue posible cargar los clientes.'
          );

        }

      });

  }

  private cargarPropiedades(): void {

    this.propiedadService
      .obtenerPropiedades()
      .subscribe({

        next: (respuesta) => {

          this.propiedades =
            respuesta.contenido;

        },

        error: () => {

          this.snackbar.error(
            'No fue posible cargar las propiedades.'
          );

        }

      });

  }

  guardar(): void {

    if (this.formulario.invalid) {

      this.formulario.markAllAsTouched();
      return;

    }

    const valores =
      this.formulario.getRawValue();

    const request: ContratoRequest = {

      idCliente:
        valores.idCliente ?? 0,

      idPropiedad:
        valores.idPropiedad ?? 0,

      valorTotal:
        valores.valorTotal ?? 0,

      cuotaInicial:
        valores.cuotaInicial ?? 0,

      numeroCuotas:
        valores.numeroCuotas ?? 0,

      fechaInicio:
        valores.fechaInicio ?? '',

      estado:
        valores.estado ?? ''

    };

    if (this.modoEdicion) {

      this.actualizar(request);
      return;

    }

    this.crear(request);

  }

  private crear(
    request: ContratoRequest
  ): void {

    this.contratoService
      .guardarContrato(request)
      .subscribe({

        next: () => {

          this.snackbar.exito(
            'Contrato registrado correctamente.'
          );

          this.dialogRef.close(true);

        },

        error: (error) => {

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible registrar el contrato.';

          this.snackbar.error(mensaje);

        }

      });

  }

  private actualizar(
    request: ContratoRequest
  ): void {

    this.contratoService
      .actualizarContrato(
        this.contratoEditar!.idContrato,
        request
      )
      .subscribe({

        next: () => {

          this.snackbar.exito(
            'Contrato actualizado correctamente.'
          );

          this.dialogRef.close(true);

        },

        error: (error) => {

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible actualizar el contrato.';

          this.snackbar.error(mensaje);

        }

      });

  }

}
