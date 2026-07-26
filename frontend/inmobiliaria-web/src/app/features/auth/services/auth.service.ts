import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';
import { environment } from '../../../../environments/environment';

interface JwtPayload {
  sub: string;
  rol: string;
  iat: number;
  exp: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = `${environment.apiUrl}/auth`;

  private readonly tokenKey = 'token';

  iniciarSesion(
    credenciales: LoginRequest
  ): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      credenciales
    );
  }

  guardarToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  eliminarToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  estaAutenticado(): boolean {

    const token = this.obtenerToken();

    if (!token) {
      return false;
    }

    const payload = this.obtenerPayload();

    if (!payload) {
      return false;
    }

    const ahora = Math.floor(Date.now() / 1000);

    return payload.exp > ahora;
  }

  obtenerCorreo(): string | null {
    return this.obtenerPayload()?.sub ?? null;
  }

  obtenerRol(): string | null {
    return this.obtenerPayload()?.rol ?? null;
  }

  esAdministrador(): boolean {
    return this.obtenerRol() === 'ADMINISTRADOR';
  }

  esEmpleado(): boolean {
    return this.obtenerRol() === 'EMPLEADO';
  }

  esCliente(): boolean {
    return this.obtenerRol() === 'CLIENTE';
  }

  private obtenerPayload(): JwtPayload | null {

    const token = this.obtenerToken();

    if (!token) {
      return null;
    }

    try {

      const partes = token.split('.');

      if (partes.length !== 3) {
        return null;
      }

      const payloadBase64 = partes[1]
        .replace(/-/g, '+')
        .replace(/_/g, '/');

      const payloadJson = decodeURIComponent(
        atob(payloadBase64)
          .split('')
          .map(
            caracter =>
              '%' +
              ('00' + caracter.charCodeAt(0).toString(16))
                .slice(-2)
          )
          .join('')
      );

      return JSON.parse(payloadJson) as JwtPayload;

    } catch (error) {

      console.error(
        'No fue posible leer el token JWT:',
        error
      );

      return null;
    }
  }
}
