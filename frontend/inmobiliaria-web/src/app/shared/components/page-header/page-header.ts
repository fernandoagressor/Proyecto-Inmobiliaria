import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-page-header',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './page-header.html',
  styleUrl: './page-header.scss'
})
export class PageHeader {

  @Input({ required: true })
  titulo!: string;

  @Input()
  textoBoton = '';

  @Input()
  icono = 'add';

  @Output()
  accion = new EventEmitter<void>();

  ejecutarAccion(): void {
    this.accion.emit();
  }
}
