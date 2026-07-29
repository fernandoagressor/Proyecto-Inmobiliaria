import { inject, Injectable } from '@angular/core';

import { AuthService } from '../../features/auth/services/auth.service';

import { MenuItem } from '../models/menu-item';

import { ADMIN_MENU } from '../data/admin-menu';
import { EMPLOYEE_MENU } from '../data/employee-menu';
import { CLIENT_MENU } from '../data/client-menu';
// Más adelante:
// import { SUPER_ADMIN_MENU } from '../data/super-admin-menu';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {

  private readonly authService = inject(AuthService);

  private readonly menus: Record<string, MenuItem[]> = {
    ADMINISTRADOR: ADMIN_MENU,
    EMPLEADO: EMPLOYEE_MENU,
    CLIENTE: CLIENT_MENU
    // SUPER_ADMIN: SUPER_ADMIN_MENU
  };

  getMenu(): MenuItem[] {

    const rol = this.authService.obtenerRol();

    if (!rol) {
      return [];
    }

    return this.menus[rol] ?? [];

  }

}
