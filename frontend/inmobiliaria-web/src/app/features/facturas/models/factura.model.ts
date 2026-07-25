export interface Factura {
  idFactura: number;
  idContrato: number;
  numeroFactura: number;
  fechaEmision: string;
  fechaVencimiento: string;
  valorFactura: number;
  estado: string;
}

export interface FacturaRequest {
  idContrato: number;
  numeroFactura: number;
  fechaEmision: string;
  fechaVencimiento: string;
  valorFactura: number;
}
