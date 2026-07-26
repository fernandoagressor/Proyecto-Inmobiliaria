import {
  Component,
  EventEmitter,
  Output,
  inject
} from '@angular/core';

import { RouterLink, RouterLinkActive } from '@angular/router';

import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';

import { SidebarService } from '../../services/sidebar.service';
import { MenuItem } from '../../models/menu-item';

import { AuthService } from '../../../features/auth/services/auth.service';

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

  @Output()
  cerrarMenu = new EventEmitter<void>();

  private readonly sidebarService =
    inject(SidebarService);

  private readonly authService =
    inject(AuthService);

  menu: MenuItem[] =
    this.sidebarService.getMenu();

  get correoUsuario(): string {
    return this.authService.obtenerCorreo()
      ?? 'Usuario';
  }

  get rolUsuario(): string {
    return this.authService.obtenerRol()
      ?? '';
  }

  emitirCerrarMenu(): void {
    this.cerrarMenu.emit();
  }

  getGrupo(nombre: string): MenuItem[] {
    return this.menu.filter(
      item => item.grupo === nombre
    );
  }
}
