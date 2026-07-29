export interface Proyecto {

  id: number;

  codigo: string;

  nombre: string;

  descripcion: string;

  estado: 'ACTIVO' | 'INACTIVO';

  fechaCreacion: Date;

}
