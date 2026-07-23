import { Component, EventEmitter, Output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { SidebarService } from '../../services/sidebar.service';
import { ADMIN_MENU } from '../../data/admin-menu';
import { MenuItem } from '../../models/menu-item';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    MatIconModule,
    MatListModule
  ],
  templateUrl: './app-sidebar.html',
  styleUrl: './app-sidebar.scss'
})
export class AppSidebar {

  @Output() cerrarMenu = new EventEmitter<void>();

  menu: MenuItem[] = [];

  constructor(
    private sidebarService: SidebarService
  ) {
    this.menu = this.sidebarService.getMenu();
  }

  emitirCerrarMenu(): void {
    this.cerrarMenu.emit();
  }

  getGrupo(nombre: string): MenuItem[] {
    return this.menu.filter(item => item.grupo === nombre);
  }

}
