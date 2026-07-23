import { inject, Injectable } from '@angular/core';
import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { PageResponse } from '../models/page-response.model';

import { ClienteActualizacionRequest } from '../models/cliente-actualizacion-request.model';
import { ClienteRequest } from '../models/cliente-request.model';
import { Cliente } from '../models/cliente.model';
import { ClienteFiltro } from '../models/cliente-filtro.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/clientes`;

  obtenerClientes(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'idCliente',
    direction: 'asc' | 'desc' = 'asc',
    filtro: ClienteFiltro = {}
  ): Observable<PageResponse<Cliente>> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);

    if (filtro.nombreCompleto?.trim()) {
      params = params.set(
        'nombreCompleto',
        filtro.nombreCompleto.trim()
      );
    }

    if (filtro.direccion?.trim()) {
      params = params.set(
        'direccion',
        filtro.direccion.trim()
      );
    }

    return this.http.get<PageResponse<Cliente>>(
      this.apiUrl,
      { params }
    );
  }
  crearCliente(cliente: ClienteRequest): Observable<Cliente> {
    return this.http.post<Cliente>(
      `${this.apiUrl}/registro`,
      cliente
    );
  }

  actualizarCliente(
    idCliente: number,
    cliente: ClienteActualizacionRequest
  ): Observable<Cliente> {

    return this.http.put<Cliente>(
      `${this.apiUrl}/${idCliente}`,
      cliente
    );
  }
  eliminarCliente(idCliente: number): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${idCliente}`
    );

  }
}
