import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Empleado } from '../models/empleado.model';
import { EmpleadoRequest } from '../models/empleado-request.model';
import { PageResponse } from '../../clientes/models/page-response.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/empleados`;

  listar(
    pagina: number = 0,
    tamano: number = 10,
    ordenarPor: string = 'idEmpleado',
    direccion: string = 'asc'
  ): Observable<PageResponse<Empleado>> {

    const params = new HttpParams()
      .set('page', pagina)
      .set('size', tamano)
      .set('sortBy', ordenarPor)
      .set('direction', direccion);

    return this.http.get<PageResponse<Empleado>>(
      this.apiUrl,
      { params }
    );
  }

  buscarPorId(
    idEmpleado: number
  ): Observable<Empleado> {

    return this.http.get<Empleado>(
      `${this.apiUrl}/${idEmpleado}`
    );
  }

  guardar(
    request: EmpleadoRequest
  ): Observable<Empleado> {

    return this.http.post<Empleado>(
      this.apiUrl,
      request
    );
  }

  actualizar(
    idEmpleado: number,
    request: EmpleadoRequest
  ): Observable<Empleado> {

    return this.http.put<Empleado>(
      `${this.apiUrl}/${idEmpleado}`,
      request
    );
  }

  eliminar(
    idEmpleado: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${idEmpleado}`
    );
  }
}
