import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';
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
        loadComponent: () =>
          import('./features/dashboard/pages/dashboard/dashboard')
            .then(c => c.Dashboard)
      },

      {
        path: 'clientes',
        loadComponent: () =>
          import('./features/clientes/pages/clientes-list/clientes-list')
            .then(c => c.ClientesList)
      },

      {
        path: 'propiedades',
        loadComponent: () =>
          import('./features/propiedades/pages/propiedades-list/propiedades-list')
            .then(c => c.PropiedadesList)
      },

      {
        path: 'contratos',
        loadComponent: () =>
          import('./features/contratos/pages/contratos-list/contratos-list')
            .then(c => c.ContratosList)
      },

      {
        path: 'pagos',
        loadComponent: () =>
          import('./features/pagos/pages/pagos-list/pagos-list')
            .then(c => c.PagosList)
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
