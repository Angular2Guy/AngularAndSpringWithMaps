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
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';

export interface GraphqlOptions {
	operationName: string;
    query: string;
    variables?: { [key: string]: any};
}

@Injectable()
export class GraphqlService {

  constructor(private http: HttpClient) { }
  
  public query<T>(options: GraphqlOptions): Observable<T> {
    return this.http
      .post<{ data: T }>(`/graphql`, {
	    operationName: options.operationName,
        query: options.query,
        variables: options.variables,
      })
      .pipe(map((d) => d.data));
  }

  public mutate<T>(options: GraphqlOptions): Observable<any> {
    return this.http
      .post<{ data: T }>(`/graphql`, {
	    operationName: options.operationName,
        query: options.query,
        variables: options.variables,
      })
      .pipe(map((d) => d.data));
  }
}
