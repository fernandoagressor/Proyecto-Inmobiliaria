import { Component, EventEmitter, Output } from '@angular/core';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './app-header.html',
  styleUrl: './app-header.scss'
})
export class AppHeader {

  @Output()
  toggleMenu = new EventEmitter<void>();

  abrirMenu(): void {
    this.toggleMenu.emit();
  }

}
