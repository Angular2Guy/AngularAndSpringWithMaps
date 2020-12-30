/**
 *    Copyright 2019 Sven Loesekann
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
import { Observable, of, from } from 'rxjs';

@Injectable()
export class BingMapsService {
	private initialized = false;	

	public initialize(apiKey: string): Observable<boolean> {
		if (this.initialized) {
			return of(true);
		}
		const callBackName = `bingmapsLib${new Date().getMilliseconds()}`;
		const scriptUrl = `https://www.bing.com/api/maps/mapcontrol?callback=${callBackName}&key=${apiKey}`;
		const script = document.createElement('script');
		script.type = 'text/javascript';
		script.async = true;
		script.defer = true;
		script.src = scriptUrl;
		script.charset = 'utf-8';
		document.head.appendChild(script);				
		const scriptPromise = new Promise<boolean>((resolve: Function, reject: Function) => {
            (window)[callBackName] = () => {
				this.initialized = true;
                resolve(true);
            };
            script.onerror = (error: Event) => { console.log(error); reject(false); };
        });
		return from(scriptPromise);
	}
	  
}
