export interface ContratoRequest {
  idCliente: number;
  idPropiedad: number;
  valorTotal: number;
  cuotaInicial: number;
  numeroCuotas: number;
  fechaInicio: string;
  estado: string;
}
