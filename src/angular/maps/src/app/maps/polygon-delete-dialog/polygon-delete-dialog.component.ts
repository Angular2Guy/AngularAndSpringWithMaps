import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export enum DialogResult {
	Ok,	Cancel, Delete
}
export interface DialogMetaData {
	polygonName: string, 
	polygonId: number
}

@Component({
  selector: 'app-polygon-delete-dialog',
  templateUrl: './polygon-delete-dialog.component.html',
  styleUrls: ['./polygon-delete-dialog.component.scss']
})
export class PolygonDeleteDialogComponent {
  dialogResults = DialogResult;
  isTestData = false;
  
  constructor(public dialogRef: MatDialogRef<PolygonDeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogMetaData) {
	this.isTestData = data.polygonId < 1000;
  }

  cancelClick(): void {
    this.dialogRef.close();
  } 

}
