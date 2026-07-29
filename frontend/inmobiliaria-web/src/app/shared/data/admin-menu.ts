import { MenuItem } from '../models/menu-item';

export const ADMIN_MENU: MenuItem[] = [

  {
    id: 'dashboard',
    titulo: 'Dashboard',
    icono: 'dashboard',
    ruta: '/dashboard',
    grupo: 'GENERAL',
    roles: ['ADMINISTRADOR'],
    orden: 1
  },

  {
    id: 'proyectos',
    titulo: 'Proyectos',
    icono: 'apartment',
    ruta: '/proyectos',
    grupo: 'GESTIÓN',
    roles: ['ADMINISTRADOR'],
    orden: 1
  },

  {
    id: 'clientes',
    titulo: 'Clientes',
    icono: 'groups',
    ruta: '/clientes',
    grupo: 'GESTIÓN',
    roles: ['ADMINISTRADOR'],
    orden: 1
  },

  {
    id: 'propiedades',
    titulo: 'Propiedades',
    icono: 'home_work',
    ruta: '/propiedades',
    grupo: 'GESTIÓN',
    roles: ['ADMINISTRADOR'],
    orden: 2
  },

  {
    id: 'empleados',
    titulo: 'Empleados',
    icono: 'badge',
    ruta: '/empleados',
    grupo: 'GESTIÓN',
    roles: ['ADMINISTRADOR'],
    orden: 3
  },

  {
    id: 'contratos',
    titulo: 'Contratos',
    icono: 'description',
    ruta: '/contratos',
    grupo: 'GESTIÓN',
    roles: ['ADMINISTRADOR'],
    orden: 4
  },

  {
    id: 'pagos',
    titulo: 'Pagos',
    icono: 'payments',
    ruta: '/pagos',
    grupo: 'FINANZAS',
    roles: ['ADMINISTRADOR'],
    orden: 1
  },

  {
    id: 'facturas',
    titulo: 'Facturas',
    icono: 'receipt_long',
    ruta: '/facturas',
    grupo: 'FINANZAS',
    roles: ['ADMINISTRADOR'],
    orden: 2
  }

];
