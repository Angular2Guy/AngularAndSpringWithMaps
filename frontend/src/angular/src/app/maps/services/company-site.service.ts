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
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { CompanySite } from "../model/company-site";
import { GraphqlService, GraphqlOptions } from "./graphql.service";

@Injectable()
export class CompanySiteService {
  constructor(private graphqlService: GraphqlService) {}

  public findById(id: number): Observable<CompanySite> {
    const options = {
      operationName: "getCompanySiteById",
      query:
        "query getCompanySiteById($id: ID!) { getCompanySiteById(id: $id) { id, title, atDate, polygons { id, fillColor, borderColor, title, longitude, latitude,rings{ id, primaryRing,locations { id, longitude, latitude }}}}}",
      variables: { id: id },
    } as GraphqlOptions;
    return this.mapResult<CompanySite, CompanySite>(
      this.graphqlService.query<CompanySite>(options),
      options.operationName
    );
  }

  public findByTitleAndYear(
    title: string,
    year: number
  ): Observable<CompanySite[]> {
    const options = {
      operationName: "getCompanySiteByTitle",
      query:
        "query getCompanySiteByTitle($title: String!, $year: Long!) { getCompanySiteByTitle(title: $title, year: $year) { id, title, atDate }}",
      variables: { title: title, year: year },
    } as GraphqlOptions;
    return this.mapResult<CompanySite[], CompanySite[]>(
      this.graphqlService.query<CompanySite[]>(options),
      options.operationName
    );
  }

  public findByTitleAndYearWithChildren(
    title: string,
    year: number
  ): Observable<CompanySite[]> {
    const options = {
      operationName: "getCompanySiteByTitle",
      query:
        "query getCompanySiteByTitle($title: String!, $year: Long!) { getCompanySiteByTitle(title: $title, year: $year) { id, title, atDate, polygons { id, fillColor, borderColor, title, longitude, latitude,rings{ id, primaryRing,locations { id, longitude, latitude}}}}}",
      variables: { title: title, year: year },
    } as GraphqlOptions;
    return this.mapResult<CompanySite[], CompanySite[]>(
      this.graphqlService.query<CompanySite[]>(options),
      options.operationName
    );
  }

  public upsertCompanySite(companySite: CompanySite): Observable<CompanySite> {
    const options = {
      operationName: "upsertCompanySite",
      query:
        "mutation upsertCompanySite($companySite: CompanySiteIn!) { upsertCompanySite(companySite: $companySite) { id, title, atDate, polygons { id, fillColor, borderColor, title, longitude, latitude,rings{ id, primaryRing,locations { id, longitude, latitude }}}}}",
      variables: { companySite: companySite },
    } as GraphqlOptions;
    return this.mapResult(
      this.graphqlService.mutate<CompanySite>(options),
      options.operationName
    );
  }

  public resetDb(): Observable<boolean> {
    const options = {
      operationName: "resetDb",
      query: "mutation resetDb { resetDb }",
    } as GraphqlOptions;
    return this.mapResult<boolean, boolean>(
      this.graphqlService.mutate<boolean>(options),
      options.operationName
    );
  }

  public deletePolygon(
    companySiteId: number,
    polygonId: number
  ): Observable<boolean> {
    const options = {
      operationName: "deletePolygon",
      query:
        "mutation deletePolygon($companySiteId: ID!, $polygonId: ID!) { deletePolygon(companySiteId: $companySiteId, polygonId: $polygonId) }",
      variables: { companySiteId: companySiteId, polygonId: polygonId },
    } as GraphqlOptions;
    return this.graphqlService.mutate<CompanySite>(options);
  }

  private mapResult<A, B>(
    serviceObs: Observable<A>,
    operationName: string
  ): Observable<B> {
    return serviceObs.pipe(
      map((config) => {
        const result = (config as unknown as any)[operationName];
        //console.log(result);
        return result;
      })
    );
  }
}
