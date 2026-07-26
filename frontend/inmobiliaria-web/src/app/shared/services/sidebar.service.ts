import { inject, Injectable } from '@angular/core';

import { AuthService } from '../../features/auth/services/auth.service';

import { MenuItem } from '../models/menu-item';

import { ADMIN_MENU } from '../data/admin-menu';
import { EMPLOYEE_MENU } from '../data/employee-menu';
import { CLIENT_MENU } from '../data/client-menu';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {

  private readonly authService = inject(AuthService);

  getMenu(): MenuItem[] {

    const rol = this.authService.obtenerRol();

    switch (rol) {

      case 'ADMINISTRADOR':
        return ADMIN_MENU;

      case 'EMPLEADO':
        return EMPLOYEE_MENU;

      case 'CLIENTE':
        return CLIENT_MENU;

      default:
        return [];
    }
  }
}
