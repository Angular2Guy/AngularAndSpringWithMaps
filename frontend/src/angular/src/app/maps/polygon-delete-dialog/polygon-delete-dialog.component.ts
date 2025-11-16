/**Copyright 2019 Sven Loesekann
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
import { CommonModule } from "@angular/common";
import { Component, Inject } from "@angular/core";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatSliderModule } from "@angular/material/slider";

// eslint-disable-next-line no-shadow
export enum MyDialogResult {
  ok,
  cancel,
  delete,
}
export interface DialogMetaData {
  polygonName: string;
  polygonId: number;
}

@Component({
    selector: "app-polygon-delete-dialog",
    imports: [
      CommonModule,
      ReactiveFormsModule,
      FormsModule,
      MatAutocompleteModule,
      MatFormFieldModule,
      MatSliderModule,
      MatInputModule,
      MatListModule,
      MatButtonModule,
      MatDialogModule
    ],
    templateUrl: "./polygon-delete-dialog.component.html",
    styleUrls: ["./polygon-delete-dialog.component.scss"],    
})
export class PolygonDeleteDialogComponent {
  protected dialogResults = MyDialogResult;
  protected isTestData = false;

  constructor(
    public dialogRef: MatDialogRef<PolygonDeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogMetaData
  ) {
    this.isTestData = data.polygonId < 1000;
  }

  cancelClick(): void {
    this.dialogRef.close();
  }
}
