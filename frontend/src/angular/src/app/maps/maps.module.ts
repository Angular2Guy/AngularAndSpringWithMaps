/**Copyright 2016 Sven Loesekann

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
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompanySiteComponent } from './company-site/company-site.component';
import { CompanySiteService } from './services/company-site.service';
import { ConfigurationService } from './services/configuration.service';
import { GraphqlService } from './services/graphql.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSliderModule } from '@angular/material/slider';
import { MapsRoutingModule } from './maps-routing.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { PolygonDeleteDialogComponent } from './polygon-delete-dialog/polygon-delete-dialog.component';

@NgModule({
	declarations: [CompanySiteComponent, PolygonDeleteDialogComponent],
	imports: [
		CommonModule,
		MapsRoutingModule,
		ReactiveFormsModule,
		FormsModule,
		MatAutocompleteModule,
		MatFormFieldModule,
		MatSliderModule,
		MatInputModule,
		MatListModule,
		MatButtonModule,
		MatDialogModule,
	],
	providers: [CompanySiteService, ConfigurationService, GraphqlService]
})
export class MapsModule { }
