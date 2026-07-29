import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';
import { MainLayout } from './shared/layouts/main-layout/main-layout';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/pages/login/login')
        .then(c => c.Login)
  },

  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],

    children: [

      {
        path: 'dashboard',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR',
            'EMPLEADO',
            'CLIENTE'
          ]
        },
        loadComponent: () =>
          import('./features/dashboard/pages/dashboard/dashboard')
            .then(c => c.Dashboard)
      },
      {
        path: 'proyectos',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR'
          ]
        },
        loadComponent: () =>
          import('./features/proyectos/pages/proyectos-list/proyectos-list')
            .then(c => c.ProyectosListComponent)
      },

      {
        path: 'clientes',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR',
            'EMPLEADO'
          ]
        },
        loadComponent: () =>
          import('./features/clientes/pages/clientes-list/clientes-list')
            .then(c => c.ClientesList)
      },

      {
        path: 'propiedades',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR',
            'EMPLEADO',
            'CLIENTE'
          ]
        },
        loadComponent: () =>
          import('./features/propiedades/pages/propiedades-list/propiedades-list')
            .then(c => c.PropiedadesList)
      },
      {
        path: 'empleados',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR'
          ]
        },
        loadComponent: () =>
          import('./features/empleados/pages/empleados-list/empleados-list')
            .then(c => c.EmpleadosList)
      },

      {
        path: 'contratos',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR',
            'EMPLEADO',
            'CLIENTE'
          ]
        },
        loadComponent: () =>
          import('./features/contratos/pages/contratos-list/contratos-list')
            .then(c => c.ContratosList)
      },

      {
        path: 'pagos',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR',
            'CLIENTE'
          ]
        },
        loadComponent: () =>
          import('./features/pagos/pages/pagos-list/pagos-list')
            .then(c => c.PagosList)
      },

      {
        path: 'facturas',
        canActivate: [roleGuard],
        data: {
          roles: [
            'ADMINISTRADOR',
            'CLIENTE'
          ]
        },
        loadComponent: () =>
          import('./features/facturas/pages/facturas-list/facturas-list')
            .then(c => c.FacturasList)
      },

      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }

    ]
  },

  {
    path: '**',
    redirectTo: ''
  }
];
