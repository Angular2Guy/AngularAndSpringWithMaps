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
