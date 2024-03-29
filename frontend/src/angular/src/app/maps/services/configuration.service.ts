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
import { MainConfiguration } from "../model/main-configuration";
import { map } from "rxjs/operators";
import { of, Observable } from "rxjs";
import { GraphqlService } from "./graphql.service";

@Injectable()
export class ConfigurationService {
  private mainConfiguration: MainConfiguration = null;

  constructor(private graphqlService: GraphqlService) {}

  public importConfiguration(): Observable<MainConfiguration> {
    if (!this.mainConfiguration) {
      const options = {
        operationName: "getMainConfiguration",
        query: "query getMainConfiguration {getMainConfiguration {mapKey}}",
      };
      return this.graphqlService.query<MainConfiguration>(options).pipe(
        map((config) => {
          this.mainConfiguration = (config as unknown as any)[
            options.operationName
          ];
          //console.log(this.mainConfiguration.mapKey);
          return this.mainConfiguration;
        })
      );
    } else {
      return of(this.mainConfiguration);
    }
  }
}
