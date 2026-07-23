export interface Contrato {
  idContrato: number;

  idCliente: number;
  nombreCliente: string;

  idPropiedad: number;
  codigoPropiedad: string;

  valorTotal: number;
  cuotaInicial: number;
  saldoPendiente: number;

  numeroCuotas: number;

  fechaInicio: string;

  estado: string;
}
