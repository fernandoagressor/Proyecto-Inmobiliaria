import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

import { EmpleadoService } from '../../services/empleado.service';
import { Empleado } from '../../models/empleado.model';
import { EmpleadoRequest } from '../../models/empleado-request.model';
import { Snackbar } from '../../../../shared/services/snackbar';

@Component({
  selector: 'app-empleado-form',
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
  templateUrl: './empleado-form.html',
  styleUrl: './empleado-form.scss'
})
export class EmpleadoForm implements OnChanges {

  private readonly fb = inject(FormBuilder);
  private readonly empleadoService = inject(EmpleadoService);
  private readonly snackbar = inject(Snackbar);

  @Input() empleado: Empleado | null = null;

  @Output() guardado = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  guardando = false;

  formulario: FormGroup = this.fb.group({
    tipoDocumento: ['', Validators.required],
    numeroDocumento: ['', Validators.required],
    nombres: ['', Validators.required],
    apellidos: ['', Validators.required],
    telefono: ['', Validators.required],
    correo: ['', [Validators.required, Validators.email]],
    cargo: ['', Validators.required],
    fechaIngreso: ['', Validators.required],
    password: ['']
  });

  get editando(): boolean {
    return this.empleado !== null;
  }

  ngOnChanges(changes: SimpleChanges): void {

    if (!changes['empleado']) {
      return;
    }

    if (this.empleado) {

      this.formulario.patchValue({
        tipoDocumento: this.empleado.tipoDocumento,
        numeroDocumento: this.empleado.numeroDocumento,
        nombres: this.empleado.nombres,
        apellidos: this.empleado.apellidos,
        telefono: this.empleado.telefono,
        correo: this.empleado.correo,
        cargo: this.empleado.cargo,
        fechaIngreso: this.empleado.fechaIngreso,
        password: ''
      });

    } else {

      this.formulario.reset({
        tipoDocumento: '',
        numeroDocumento: '',
        nombres: '',
        apellidos: '',
        telefono: '',
        correo: '',
        cargo: '',
        fechaIngreso: '',
        password: ''
      });
    }
  }

  guardar(): void {

    if (!this.editando) {
      this.formulario
        .get('password')
        ?.setValidators([
          Validators.required,
          Validators.minLength(6)
        ]);
    } else {
      this.formulario
        .get('password')
        ?.setValidators([
          Validators.minLength(6)
        ]);
    }

    this.formulario
      .get('password')
      ?.updateValueAndValidity();

    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const request: EmpleadoRequest = {
      tipoDocumento: this.formulario.value.tipoDocumento,
      numeroDocumento: this.formulario.value.numeroDocumento,
      nombres: this.formulario.value.nombres,
      apellidos: this.formulario.value.apellidos,
      telefono: this.formulario.value.telefono,
      correo: this.formulario.value.correo,
      cargo: this.formulario.value.cargo,
      fechaIngreso: this.formulario.value.fechaIngreso,
      password: this.formulario.value.password || ''
    };

    this.guardando = true;

    const peticion = this.editando
      ? this.empleadoService.actualizar(
          this.empleado!.idEmpleado,
          request
        )
      : this.empleadoService.guardar(request);

    peticion.subscribe({
      next: () => {

        this.guardando = false;

        this.snackbar.exito(
          this.editando
            ? 'Empleado actualizado correctamente'
            : 'Empleado creado correctamente'
        );

        this.guardado.emit();
      },

      error: (error) => {

        console.error(
          'Error al guardar empleado:',
          error
        );

        this.guardando = false;

        this.snackbar.error(
          error?.error?.mensaje ||
          'No fue posible guardar el empleado'
        );
      }
    });
  }

  cerrar(): void {
    this.cancelar.emit();
  }
}
