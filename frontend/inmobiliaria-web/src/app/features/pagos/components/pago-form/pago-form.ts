import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit
} from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  MatDialogModule,
  MatDialogRef
} from '@angular/material/dialog';

import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { Contrato } from '../../../contratos/models/contrato.model';
import { ContratoService } from '../../../contratos/services/contrato.service';

import { Snackbar } from '../../../../shared/services/snackbar';

import { PagoRequest } from '../../models/pago-request.model';
import { PagoService } from '../../services/pago.service';

@Component({
  selector: 'app-pago-form',
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
  templateUrl: './pago-form.html',
  styleUrl: './pago-form.scss'
})
export class PagoForm implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly pagoService = inject(PagoService);
  private readonly contratoService = inject(ContratoService);
  private readonly snackbar = inject(Snackbar);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly dialogRef =
    inject(MatDialogRef<PagoForm>);

  contratos: Contrato[] = [];
  guardando = false;

  formulario = this.fb.group({

    idContrato: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    numeroCuota: [
      1,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    fechaPago: [
      '',
      Validators.required
    ],

    valorPago: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    medioPago: [
      '',
      Validators.required
    ],

    observacion: ['']

  });

  ngOnInit(): void {
    this.cargarContratos();
  }

  private cargarContratos(): void {

    this.contratoService
      .obtenerContratos()
      .subscribe({

        next: (respuesta) => {

          this.contratos =
            respuesta.contenido.filter(
              contrato =>
                contrato.estado.toUpperCase() === 'VIGENTE'
            );

          this.cdr.detectChanges();
        },

        error: (error: any) => {

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible cargar los contratos.';

          this.snackbar.error(mensaje);

        }

      });

  }

  guardar(): void {

    if (this.formulario.invalid || this.guardando) {

      this.formulario.markAllAsTouched();
      return;

    }

    const valores =
      this.formulario.getRawValue();

    const request: PagoRequest = {

      idContrato:
        valores.idContrato ?? 0,

      numeroCuota:
        valores.numeroCuota ?? 0,

      fechaPago:
        valores.fechaPago ?? '',

      valorPago:
        valores.valorPago ?? 0,

      medioPago:
        valores.medioPago ?? '',

      observacion:
        valores.observacion ?? ''

    };

    this.guardando = true;

    this.pagoService
      .guardarPago(request)
      .subscribe({

        next: () => {

          this.snackbar.exito(
            'Pago registrado correctamente.'
          );

          this.dialogRef.close(true);

        },

        error: (error: any) => {

          this.guardando = false;

          const mensaje =
            error?.error?.mensaje ??
            'No fue posible registrar el pago.';

          this.snackbar.error(mensaje);

        }

      });

  }

}
