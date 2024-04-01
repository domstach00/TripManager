import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { MapIcon } from "../../_model/MapPinIcons";

export interface IconKeyUrl {
  key: string,
  url: string,
}

@Component({
  selector: 'app-select-icon-dialog',
  templateUrl: './select-icon-dialog.component.html',
  styleUrls: ['./select-icon-dialog.component.scss']
})
export class SelectIconDialogComponent {
  icons: IconKeyUrl[];
  constructor(
      public dialogRef: MatDialogRef<SelectIconDialogComponent>,
      @Inject(MAT_DIALOG_DATA) readonly iconEnum: MapIcon,
  ) {
    this.icons = Object.keys(MapIcon).map(keyValue => ({key: keyValue, url: MapIcon[keyValue as keyof typeof MapIcon]}));
  }

  onClose() {
    this.dialogRef.close();
  }

  selectIcon(icon: any) {
    this.dialogRef.close(icon);
  }
}
