import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';

import { Propiedad } from '../models/propiedad.model';
import { PropiedadRequest } from '../models/propiedad-request.model';
import { PageResponse } from '../../clientes/models/page-response.model';

@Injectable({
  providedIn: 'root'
})
export class PropiedadService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/propiedades`;

  obtenerPropiedades():
    Observable<PageResponse<Propiedad>> {

    return this.http.get<PageResponse<Propiedad>>(
      this.apiUrl
    );

  }

  obtenerPropiedadPorId(
    idPropiedad: number
  ): Observable<Propiedad> {

    return this.http.get<Propiedad>(
      `${this.apiUrl}/${idPropiedad}`
    );

  }

  guardarPropiedad(
    request: PropiedadRequest
  ): Observable<Propiedad> {

    return this.http.post<Propiedad>(
      this.apiUrl,
      request
    );

  }

  actualizarPropiedad(
    idPropiedad: number,
    request: PropiedadRequest
  ): Observable<Propiedad> {

    return this.http.put<Propiedad>(
      `${this.apiUrl}/${idPropiedad}`,
      request
    );

  }

  eliminarPropiedad(
    idPropiedad: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${idPropiedad}`
    );

  }

}
