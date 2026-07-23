export interface PageResponse<T> {

  contenido: T[];

  paginaActual: number;

  tamanoPagina: number;

  totalRegistros: number;

  totalPaginas: number;

  primera: boolean;

  ultima: boolean;

}
