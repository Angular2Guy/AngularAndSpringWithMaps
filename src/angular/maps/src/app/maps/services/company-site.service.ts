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
import { Injectable } from '@angular/core';
import { MapsModule } from '../maps.module';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompanySite } from '../model/company-site';

@Injectable()
export class CompanySiteService {

  constructor(private http: HttpClient) { }

  public findById(id: number): Observable<CompanySite> {
	return this.http.get<CompanySite>(`/rest/companySite/id/${id}`);
  }

  public findByTitle(title: string): Observable<CompanySite> {
	return this.http.get<CompanySite>(`/rest/companySite/title/${title}`)
  }
}
