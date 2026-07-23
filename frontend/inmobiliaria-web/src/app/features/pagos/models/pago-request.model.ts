export interface PagoRequest {
  idContrato: number;
  numeroCuota: number;
  fechaPago: string;
  valorPago: number;
  medioPago: string;
  observacion: string;
}
