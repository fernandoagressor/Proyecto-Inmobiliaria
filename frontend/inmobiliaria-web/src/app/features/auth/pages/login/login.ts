import { Component, inject } from '@angular/core';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { Router } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/login-request.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {

  private readonly authService = inject(AuthService);

  private readonly router = inject(Router);

  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {

    this.loginForm = this.formBuilder.group({

      correo: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      password: [
        '',
        [
          Validators.required,
          Validators.minLength(6)
        ]
      ]

    });

  }

  iniciarSesion(): void {

    if (this.loginForm.invalid) {

      this.loginForm.markAllAsTouched();

      return;

    }

    const credenciales: LoginRequest = {

      correo: this.loginForm.value.correo,

      password: this.loginForm.value.password

    };

    this.authService.iniciarSesion(credenciales)
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Login correcto',
            respuesta
          );

          this.authService.guardarToken(
            respuesta.token
          );

          console.log(
            'Token guardado correctamente'
          );

          this.router.navigateByUrl('/dashboard', {
            replaceUrl: true
          });

        },

        error: (error) => {

          console.error(
            'Error al iniciar sesión:',
            error
          );

        }

      });

  }

}
