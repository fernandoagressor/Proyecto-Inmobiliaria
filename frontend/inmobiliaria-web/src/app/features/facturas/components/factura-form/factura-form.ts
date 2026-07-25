import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  inject
} from '@angular/core';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Snackbar } from '../../../../shared/services/snackbar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

import {
  Factura,
  FacturaRequest
} from '../../models/factura.model';

import { FacturaService } from '../../services/factura.service';

import { ContratoService } from '../../../contratos/services/contrato.service';
import { Contrato } from '../../../contratos/models/contrato.model';

@Component({
  selector: 'app-factura-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule
  ],
  templateUrl: './factura-form.html',
  styleUrl: './factura-form.scss'
})
export class FacturaForm implements OnInit, OnChanges {

  private readonly fb = inject(FormBuilder);

  private readonly facturaService =
    inject(FacturaService);

  private readonly contratoService =
    inject(ContratoService);

  private readonly cdr = inject(ChangeDetectorRef);

  private readonly snackbar = inject(Snackbar);

  @Input()
  factura: Factura | null = null;

  @Output()
  guardado = new EventEmitter<void>();

  @Output()
  cancelar = new EventEmitter<void>();

  contratos: Contrato[] = [];

  cargandoContratos = false;

  guardando = false;

  formulario: FormGroup = this.fb.group({

    idContrato: [
      null,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    numeroFactura: [
      null,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    fechaEmision: [
      '',
      Validators.required
    ],

    fechaVencimiento: [
      '',
      Validators.required
    ],

    valorFactura: [
      null,
      [
        Validators.required,
        Validators.min(1)
      ]
    ]

  });

  ngOnInit(): void {
    this.cargarContratos();
  }

  ngOnChanges(
    changes: SimpleChanges
  ): void {

    if (changes['factura']) {
      this.cargarFactura();
    }
  }

  get esEdicion(): boolean {
    return this.factura !== null;
  }

  guardar(): void {

    if (this.formulario.invalid) {

      this.formulario.markAllAsTouched();

      return;
    }

    const request: FacturaRequest = {

      idContrato: Number(
        this.formulario.value.idContrato
      ),

      numeroFactura: Number(
        this.formulario.value.numeroFactura
      ),

      fechaEmision:
        this.formulario.value.fechaEmision,

      fechaVencimiento:
        this.formulario.value.fechaVencimiento,

      valorFactura: Number(
        this.formulario.value.valorFactura
      )

    };

    this.guardando = true;

    const operacion = this.esEdicion

      ? this.facturaService.actualizar(
          this.factura!.idFactura,
          request
        )

      : this.facturaService.crear(
          request
        );

    operacion.subscribe({

      next: () => {
        this.guardando = false;

        this.snackbar.exito(
          this.esEdicion
            ? 'Factura actualizada correctamente'
            : 'Factura creada correctamente'
        );

        this.guardado.emit();

        this.formulario.reset();

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error(
          'Error al guardar la factura:',
          error
        );

        this.guardando = false;

        const mensaje =
          error?.error?.mensaje ??
          'No fue posible guardar la factura';

        this.snackbar.error(mensaje);

        this.cdr.detectChanges();
      }

    });
  }

  cerrar(): void {

    this.formulario.reset();

    this.cancelar.emit();
  }

  private cargarContratos(): void {

    this.cargandoContratos = true;

    this.contratoService.obtenerContratos(
      0,
      100,
      'idContrato',
      'asc'
    )
    .subscribe({

      next: (respuesta) => {

        this.contratos =
          respuesta.contenido;

        this.cargandoContratos = false;
      },

      error: (error) => {

        console.error(
          'Error al cargar los contratos:',
          error
        );

        this.cargandoContratos = false;
      },

      complete: () => {
        this.cargandoContratos = false;
      }

    });
  }

  private cargarFactura(): void {

    if (!this.factura) {

      this.formulario.reset();

      return;
    }

    this.formulario.patchValue({

      idContrato:
        this.factura.idContrato,

      numeroFactura:
        this.factura.numeroFactura,

      fechaEmision:
        this.factura.fechaEmision,

      fechaVencimiento:
        this.factura.fechaVencimiento,

      valorFactura:
        this.factura.valorFactura

    });
  }
}
