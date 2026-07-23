import {
  Component,
  inject
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Cliente } from '../../models/cliente.model';
import { ClienteActualizacionRequest } from '../../models/cliente-actualizacion-request.model';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef
} from '@angular/material/dialog';

import { ClienteService } from '../../services/cliente.service';
import { ClienteRequest } from '../../models/cliente-request.model';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDialogModule,
    MatSnackBarModule
  ],
  templateUrl: './cliente-form.html',
  styleUrl: './cliente-form.scss'
})
export class ClienteForm {

  private readonly clienteService = inject(ClienteService);

  private readonly dialogRef = inject(MatDialogRef<ClienteForm>);
  private readonly formBuilder = inject(FormBuilder);

  clienteForm = this.formBuilder.group({
    tipoDocumento: [
      '',
      [
        Validators.required
      ]
    ],

    numeroDocumento: [
      '',
      [
        Validators.required,
        Validators.pattern(/^[0-9]+$/),
        Validators.maxLength(20)
      ]
    ],

    nombres: [
      '',
      [
        Validators.required,
        Validators.maxLength(100),
        Validators.pattern(/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/)
      ]
    ],

    apellidos: [
      '',
      [
        Validators.required,
        Validators.maxLength(100),
        Validators.pattern(/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/)
      ]
    ],

    telefono: [
      '',
      [
        Validators.required,
        Validators.pattern(/^[0-9]{10}$/)
      ]
    ],

    correo: [
      '',
      [
        Validators.required,
        Validators.email,
        Validators.maxLength(100)
      ]
    ],

    direccion: [
      '',
      [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(200)
      ]
    ]
  });

  private readonly snackBar = inject(MatSnackBar);

  readonly clienteEditar = inject<Cliente | null>(
    MAT_DIALOG_DATA,
    { optional: true }
  );

  readonly modoEdicion = this.clienteEditar !== null;

  constructor() {

    if (this.modoEdicion && this.clienteEditar) {

      this.clienteForm.patchValue({
        telefono: this.clienteEditar.telefono,
        correo: this.clienteEditar.correo,
        direccion: this.clienteEditar.direccion
      });

      this.clienteForm.controls.tipoDocumento.disable();
      this.clienteForm.controls.numeroDocumento.disable();
      this.clienteForm.controls.nombres.disable();
      this.clienteForm.controls.apellidos.disable();

      this.clienteForm.controls.tipoDocumento.clearValidators();
      this.clienteForm.controls.numeroDocumento.clearValidators();
      this.clienteForm.controls.nombres.clearValidators();
      this.clienteForm.controls.apellidos.clearValidators();

      this.clienteForm.controls.tipoDocumento.updateValueAndValidity();
      this.clienteForm.controls.numeroDocumento.updateValueAndValidity();
      this.clienteForm.controls.nombres.updateValueAndValidity();
      this.clienteForm.controls.apellidos.updateValueAndValidity();
    }

  }


  guardar(): void {

    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    const valores = this.clienteForm.getRawValue();

    if (this.modoEdicion && this.clienteEditar) {

      const clienteActualizacion: ClienteActualizacionRequest = {
        telefono: valores.telefono!,
        correo: valores.correo!,
        direccion: valores.direccion!
      };

      this.clienteService.actualizarCliente(
        this.clienteEditar.idCliente,
        clienteActualizacion
      ).subscribe({

        next: () => {

          this.mostrarMensaje(
            'Cliente actualizado correctamente.'
          );

          this.dialogRef.close(true);
        },

        error: (error) => {

          console.error(
            'Error al actualizar el cliente:',
            error
          );

          this.manejarError(
            error,
            'Ocurrió un error al actualizar el cliente.'
          );
        }

      });

      return;
    }

    const clienteRequest: ClienteRequest = {
      tipoDocumento: valores.tipoDocumento!,
      numeroDocumento: valores.numeroDocumento!,
      nombres: valores.nombres!,
      apellidos: valores.apellidos!,
      telefono: valores.telefono!,
      correo: valores.correo!,
      direccion: valores.direccion!
    };

    this.clienteService.crearCliente(clienteRequest)
      .subscribe({

        next: () => {
          this.dialogRef.close(true);
        },

        error: (error) => {
          console.error(
            'Error al crear el cliente:',
            error
          );
        }

      });

  }
  private mostrarMensaje(
    mensaje: string,
    duracion: number = 3500
  ): void {

    this.snackBar.open(
      mensaje,
      'Cerrar',
      {
        duration: duracion,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      }
    );
  }

  private manejarError(
    error: any,
    mensajePredeterminado: string
  ): void {

    const mensajeBackend =
      error?.error?.mensaje ||
      error?.error?.message;

    if (error.status === 409) {
      this.mostrarMensaje(
        mensajeBackend ||
        'El documento o el correo ya se encuentra registrado.'
      );
      return;
    }

    if (error.status === 400) {
      this.mostrarMensaje(
        mensajeBackend ||
        'Verifica la información ingresada.'
      );
      return;
    }

    this.mostrarMensaje(
      mensajeBackend ||
      mensajePredeterminado
    );
  }

}
