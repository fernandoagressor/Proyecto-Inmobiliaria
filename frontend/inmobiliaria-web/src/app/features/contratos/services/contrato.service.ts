import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Contrato } from '../models/contrato.model';
import { ContratoRequest } from '../models/contrato-request.model';
import { PageResponse } from '../../clientes/models/page-response.model';

@Injectable({
  providedIn: 'root'
})
export class ContratoService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/contratos';

  obtenerContratos(
    pagina: number = 0,
    tamano: number = 10,
    ordenarPor: string = 'idContrato',
    direccion: string = 'asc'
  ): Observable<PageResponse<Contrato>> {

    const params = new HttpParams()
      .set('page', pagina)
      .set('size', tamano)
      .set('sort', `${ordenarPor},${direccion}`);

    return this.http.get<PageResponse<Contrato>>(
      this.apiUrl,
      { params }
    );

  }

  obtenerContratoPorId(
    idContrato: number
  ): Observable<Contrato> {

    return this.http.get<Contrato>(
      `${this.apiUrl}/${idContrato}`
    );

  }

  guardarContrato(
    request: ContratoRequest
  ): Observable<Contrato> {

    return this.http.post<Contrato>(
      this.apiUrl,
      request
    );

  }

  actualizarContrato(
    idContrato: number,
    request: ContratoRequest
  ): Observable<Contrato> {

    return this.http.put<Contrato>(
      `${this.apiUrl}/${idContrato}`,
      request
    );

  }

  eliminarContrato(
    idContrato: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${idContrato}`
    );

  }

}
