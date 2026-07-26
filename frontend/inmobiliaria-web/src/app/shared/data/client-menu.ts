import { MenuItem } from '../models/menu-item';

export const CLIENT_MENU: MenuItem[] = [
  {
    titulo: 'Mi panel',
    icono: 'dashboard',
    ruta: '/dashboard',
    grupo: 'GENERAL'
  },

  {
    titulo: 'Mis propiedades',
    icono: 'home_work',
    ruta: '/propiedades',
    grupo: 'MI INFORMACIÓN'
  },

  {
    titulo: 'Mis contratos',
    icono: 'description',
    ruta: '/contratos',
    grupo: 'MI INFORMACIÓN'
  },

  {
    titulo: 'Mis pagos',
    icono: 'payments',
    ruta: '/pagos',
    grupo: 'FINANZAS'
  },

  {
    titulo: 'Mis facturas',
    icono: 'receipt_long',
    ruta: '/facturas',
    grupo: 'FINANZAS'
  }
];
