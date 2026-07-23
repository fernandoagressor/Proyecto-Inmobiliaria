import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

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

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

import { PropiedadService } from '../../services/propiedad.service';
import { Propiedad } from '../../models/propiedad.model';
import { PropiedadRequest } from '../../models/propiedad-request.model';

import { Snackbar } from '../../../../shared/services/snackbar';

@Component({
  selector: 'app-propiedad-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule
  ],
  templateUrl: './propiedad-form.html',
  styleUrl: './propiedad-form.scss'
})
export class PropiedadForm {

  private readonly fb = inject(FormBuilder);

  private readonly propiedadService =
    inject(PropiedadService);

  private readonly snackbar =
    inject(Snackbar);

  readonly propiedadEditar =
    inject<Propiedad | null>(
      MAT_DIALOG_DATA,
      { optional: true }
    );

  readonly dialogRef =
    inject(MatDialogRef<PropiedadForm>);

  readonly modoEdicion =
    this.propiedadEditar !== null;

  formulario = this.fb.group({

    codigo: [
      '',
      [
        Validators.required,
        Validators.maxLength(30)
      ]
    ],

    titulo: [
      '',
      [
        Validators.required,
        Validators.maxLength(150)
      ]
    ],

    descripcion: [''],

    direccion: [
      '',
      [
        Validators.required,
        Validators.maxLength(200)
      ]
    ],

    valor: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    estado: [
      '',
      Validators.required
    ]

  });

  constructor() {

    if (!this.propiedadEditar) {
      return;
    }

    this.formulario.patchValue({

      codigo:
        this.propiedadEditar.codigo,

      titulo:
        this.propiedadEditar.titulo,

      descripcion:
        this.propiedadEditar.descripcion,

      direccion:
        this.propiedadEditar.direccion,

      valor:
        this.propiedadEditar.valor,

      estado:
        this.propiedadEditar.estado

    });

  }

  guardar(): void {

    if (this.formulario.invalid) {

      this.formulario.markAllAsTouched();
      return;

    }

    const valores = this.formulario.getRawValue();

    const request: PropiedadRequest = {
      codigo: valores.codigo ?? '',
      titulo: valores.titulo ?? '',
      descripcion: valores.descripcion ?? '',
      direccion: valores.direccion ?? '',
      valor: valores.valor ?? 0,
      estado: valores.estado ?? ''
    };

    if (this.modoEdicion) {

      this.actualizar(request);
      return;

    }

    this.crear(request);

  }

  private crear(
    request: PropiedadRequest
  ): void {

    this.propiedadService
      .guardarPropiedad(request)
      .subscribe({

        next: () => {

          this.snackbar.exito(
            'Propiedad registrada correctamente.'
          );

          this.dialogRef.close(true);

        },

        error: (error) => {

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible registrar la propiedad.';

          this.snackbar.error(mensaje);

        }

      });

  }

  private actualizar(
    request: PropiedadRequest
  ): void {

    this.propiedadService
      .actualizarPropiedad(
        this.propiedadEditar!.idPropiedad,
        request
      )
      .subscribe({

        next: () => {

          this.snackbar.exito(
            'Propiedad actualizada correctamente.'
          );

          this.dialogRef.close(true);

        },

        error: (error) => {

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible actualizar la propiedad.';

          this.snackbar.error(mensaje);

        }

      });

  }

}
