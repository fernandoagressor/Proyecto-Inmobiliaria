import { Component, inject } from '@angular/core';

import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef
} from '@angular/material/dialog';

import { MatButtonModule } from '@angular/material/button';

export interface ConfirmDialogData {

  titulo: string;

  mensaje: string;

  textoAceptar?: string;

  textoCancelar?: string;

}

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule
  ],
  templateUrl: './confirm-dialog.html',
  styleUrl: './confirm-dialog.scss'
})
export class ConfirmDialog {

  readonly data = inject<ConfirmDialogData>(
    MAT_DIALOG_DATA
  );

  private readonly dialogRef =
    inject(MatDialogRef<ConfirmDialog>);

  cancelar(): void {
    this.dialogRef.close(false);
  }

  aceptar(): void {
    this.dialogRef.close(true);
  }

}
