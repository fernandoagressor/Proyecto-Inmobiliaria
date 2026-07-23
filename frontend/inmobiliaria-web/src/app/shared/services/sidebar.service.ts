import { Injectable } from '@angular/core';

import { MenuItem } from '../models/menu-item';
import { ADMIN_MENU } from '../data/admin-menu';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {

  getMenu(): MenuItem[] {
    return ADMIN_MENU;
  }

}
