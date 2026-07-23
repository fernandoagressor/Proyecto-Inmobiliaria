import { MenuItem } from '../models/menu-item';

export const ADMIN_MENU: MenuItem[] = [
  {
    titulo: 'Dashboard',
    icono: 'dashboard',
    ruta: '/dashboard',
    grupo: 'GENERAL'
  },

  {
    titulo: 'Clientes',
    icono: 'groups',
    ruta: '/clientes',
    grupo: 'GESTIÓN'
  },

  {
    titulo: 'Propiedades',
    icono: 'home_work',
    ruta: '/propiedades',
    grupo: 'GESTIÓN'
  },

  {
    titulo: 'Contratos',
    icono: 'description',
    ruta: '/contratos',
    grupo: 'GESTIÓN'
  },

  {
    titulo: 'Pagos',
    icono: 'payments',
    ruta: '/pagos',
    grupo: 'FINANZAS'
  }
];
