import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { PageResponse } from '../../clientes/models/page-response.model';
import { Pago } from '../models/pago.model';
import { PagoRequest } from '../models/pago-request.model';

@Injectable({
  providedIn: 'root'
})
export class PagoService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/pagos`;

  obtenerPagos(
    page = 0,
    size = 10,
    sortBy = 'idPago',
    direction = 'asc'
  ): Observable<PageResponse<Pago>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<PageResponse<Pago>>(
      this.apiUrl,
      { params }
    );
  }

  obtenerPagoPorId(
    idPago: number
  ): Observable<Pago> {

    return this.http.get<Pago>(
      `${this.apiUrl}/${idPago}`
    );
  }

  obtenerPagosPorContrato(
    idContrato: number
  ): Observable<Pago[]> {

    return this.http.get<Pago[]>(
      `${this.apiUrl}/contrato/${idContrato}`
    );
  }

  guardarPago(
    request: PagoRequest
  ): Observable<Pago> {

    return this.http.post<Pago>(
      this.apiUrl,
      request
    );
  }

  anularPago(
    idPago: number
  ): Observable<Pago> {

    return this.http.patch<Pago>(
      `${this.apiUrl}/${idPago}/anular`,
      {}
    );
  }

}
