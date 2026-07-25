import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Factura, FacturaRequest } from '../models/factura.model';

export interface PageResponse<T> {
  contenido: T[];
  paginaActual: number;
  tamanoPagina: number;
  totalRegistros: number;
  totalPaginas: number;
  primera: boolean;
  ultima: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class FacturaService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/facturas';

  listar(
    page = 0,
    size = 10,
    sortBy = 'idFactura',
    direction = 'asc'
  ): Observable<PageResponse<Factura>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<PageResponse<Factura>>(
      this.apiUrl,
      { params }
    );
  }

  buscarPorId(idFactura: number): Observable<Factura> {
    return this.http.get<Factura>(
      `${this.apiUrl}/${idFactura}`
    );
  }

  listarPorContrato(idContrato: number): Observable<Factura[]> {
    return this.http.get<Factura[]>(
      `${this.apiUrl}/contrato/${idContrato}`
    );
  }

  crear(request: FacturaRequest): Observable<Factura> {
    return this.http.post<Factura>(
      this.apiUrl,
      request
    );
  }

  actualizar(
    idFactura: number,
    request: FacturaRequest
  ): Observable<Factura> {

    return this.http.put<Factura>(
      `${this.apiUrl}/${idFactura}`,
      request
    );
  }

  eliminar(idFactura: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${idFactura}`
    );
  }
}
