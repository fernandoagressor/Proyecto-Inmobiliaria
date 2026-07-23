export interface Pago {
  idPago: number;
  idContrato: number;

  nombreCliente: string;
  codigoPropiedad: string;
  estadoContrato: string;

  numeroCuota: number;
  fechaPago: string;
  valorPago: number;
  medioPago: string;
  observacion: string | null;
  saldoPendiente: number;
}
