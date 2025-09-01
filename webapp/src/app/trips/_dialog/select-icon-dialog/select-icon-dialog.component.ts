import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MapIcon } from '../../_model/MapPinIcons';

export interface IconItem {
	key: string;
	url: string;
}

@Component({
    selector: 'app-select-icon-dialog',
    templateUrl: './select-icon-dialog.component.html',
    styleUrls: ['./select-icon-dialog.component.scss'],
    standalone: false
})
export class SelectIconDialogComponent {
	icons: IconItem[];
	selectedKey: string | null = null;

	constructor(
		public dialogRef: MatDialogRef<SelectIconDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public currentIcon: string
	) {
		this.icons = Object.entries(MapIcon).map(
			([key, url]) => ({ key, url })
		);
		this.selectedKey = currentIcon;
	}

	selectIcon(item: IconItem) {
		this.selectedKey = item.key;
	}

	confirm() {
		const chosen: IconItem = this.icons.find(i => i.key === this.selectedKey);
		this.dialogRef.close(chosen);
	}

	cancel() {
		this.dialogRef.close();
	}
}
