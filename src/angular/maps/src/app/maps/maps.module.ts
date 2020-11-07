/**
 *    Copyright 2016 Sven Loesekann

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
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSliderModule } from '@angular/material/slider';
import { MapsRoutingModule } from './maps-routing.module';

@NgModule({
	declarations: [CompanySiteComponent],
	imports: [
		CommonModule,
		MapsRoutingModule,
		MatAutocompleteModule,
		MatFormFieldModule,
		MatSliderModule,
	],
	providers: [CompanySiteService, ConfigurationService]
})
export class MapsModule { }
