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
/* eslint-disable @typescript-eslint/naming-convention */
import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit,
  OnDestroy,
  DestroyRef,
  inject,
} from "@angular/core";
import { CompanySiteService } from "../services/company-site.service";
import "bingmaps";
import { ConfigurationService } from "../services/configuration.service";
import { MainConfiguration } from "../model/main-configuration";
import { Observable, of, iif, Subject, forkJoin } from "rxjs";
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CompanySite } from "../model/company-site";
import { FormBuilder, Validators } from "@angular/forms";
import {
  switchMap,
  debounceTime,
  filter,
  flatMap,
  tap,
  map,
} from "rxjs/operators";
import { BingMapsService } from "../../services/bing-maps.service";
import { MatSelectionListChange } from "@angular/material/list";
import { Polygon } from "../model/polygon";
import { Location } from "../model/location";
import { Ring } from "../model/ring";
import { MatDialog } from "@angular/material/dialog";
import {
  PolygonDeleteDialogComponent,
  MyDialogResult,
  DialogMetaData,
} from "../polygon-delete-dialog/polygon-delete-dialog.component";

interface Container {
  companySite: CompanySite;
  mainConfiguration: MainConfiguration;
}

interface NewLocation {
  location: Microsoft.Maps.Location;
  selected: boolean;
  id: number;
}

interface PolygonMetaData {
  companySiteId: number;
  polygonId: number;
}

@Component({
    selector: "app-company-site",
    templateUrl: "./company-site.component.html",
    styleUrls: ["./company-site.component.scss"],
    standalone: false
})
export class CompanySiteComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild("bingMap")
  protected bingMapContainer: ElementRef;

  protected newLocations: NewLocation[] = [];
  protected map: Microsoft.Maps.Map = null;
  protected resetInProgress = false;

  protected companySiteOptions: Observable<CompanySite[]>;
  protected componentForm = this.formBuilder.group({
    companySite: ["Finkenwerder" as string | CompanySite, Validators.required],
    sliderYear: [2020],
    property: ["add Property", Validators.required],
  });

  protected readonly COMPANY_SITE = "companySite";
  protected readonly SLIDER_YEAR = "sliderYear";
  protected readonly PROPERTY = "property";
  private mainConfiguration: MainConfiguration = null;
  private readonly containerInitSubject = new Subject<Container>();  
  private readonly destroy: DestroyRef = inject(DestroyRef);

  constructor(
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
    private bingMapsService: BingMapsService,
    private companySiteService: CompanySiteService,
    private configurationService: ConfigurationService
  ) {}

  ngOnInit(): void {
    this.companySiteOptions = this.componentForm.valueChanges.pipe(
      debounceTime(300),
      switchMap(() =>
        iif(
          () =>
            !this.getCompanySiteTitle() ||
            this.getCompanySiteTitle().length < 3 ||
            !this.componentForm.get(this.SLIDER_YEAR).value,
          of<CompanySite[]>([]),
          this.companySiteService.findByTitleAndYear(
            this.getCompanySiteTitle(),
            this.componentForm.get(this.SLIDER_YEAR).value
          )
        )
      ),
      takeUntilDestroyed(this.destroy)
    );
    this.componentForm.controls[
      this.SLIDER_YEAR
    ].valueChanges
      .pipe(
        debounceTime(500),
        filter(
          (year) =>
            !(
              typeof this.componentForm.get(this.COMPANY_SITE).value ===
              "string"
            )
        ),
        switchMap((year) =>
          this.companySiteService.findByTitleAndYearWithChildren(
            this.getCompanySiteTitle(),
            year as number
          )
        ),
        filter(
          (companySite) =>
            companySite?.length &&
            companySite.length > 0 &&
            companySite[0].polygons.length > 0
        ),
        takeUntilDestroyed(this.destroy)
      )
      .subscribe((companySite) => this.updateMap(companySite[0]));
    forkJoin([
      this.configurationService.importConfiguration(),
      this.companySiteService.findByTitleAndYearWithChildren(
        this.getCompanySiteTitle(),
        this.componentForm.controls[this.SLIDER_YEAR].value
      ),
    ]).pipe(takeUntilDestroyed(this.destroy)).subscribe((values) => {
      this.mainConfiguration = values[0];
      this.containerInitSubject.next({
        companySite: values[1][0],
        mainConfiguration: values[0],
      } as Container);
    });
  }

  ngAfterViewInit(): void {
    this.containerInitSubject
      .pipe(
        filter(
          (myContainer) =>
            !!myContainer &&
            !!myContainer.companySite &&
            !!myContainer.companySite.polygons &&
            !!myContainer.mainConfiguration
        ),
        flatMap((myContainer) =>
          this.bingMapsService
            .initialize(myContainer.mainConfiguration.mapKey)
            .pipe(flatMap(() => of(myContainer)))
        ),
        takeUntilDestroyed(this.destroy)
      )
      .subscribe((container) => {
        const mapOptions =
          container.companySite.polygons.length < 1
            ? ({} as Microsoft.Maps.IMapLoadOptions)
            : ({
                center: new Microsoft.Maps.Location(
                  container.companySite.polygons[0].latitude,
                  container.companySite.polygons[0].longitude
                ),
              } as Microsoft.Maps.IMapLoadOptions);
        this.map = new Microsoft.Maps.Map(
          this.bingMapContainer.nativeElement as HTMLElement,
          mapOptions
        );
        this.componentForm.controls[this.COMPANY_SITE].setValue(
          container.companySite
        );
        container.companySite.polygons.forEach((polygon) =>
          this.addPolygon(polygon)
        );
        Microsoft.Maps.Events.addHandler(this.map, "click", (e) =>
          this.onMapClick(e)
        );
      });
  }

  ngOnDestroy(): void {
    this.containerInitSubject.complete();
    this.map.dispose();
  }

  newLocationsChanged(e: MatSelectionListChange): void {
    for (let i = 0; i < e.source.options.length; i++) {
      const myOption = e.source.options.toArray()[i];
      if (e.options.includes(myOption)) {
        this.newLocations[i].selected =
          e.options[e.options.indexOf(myOption)].selected;
      }
    }
    this.updateMapPushPins();
  }

  upsertCompanySite(): void {
    if (typeof this.componentForm.get(this.COMPANY_SITE).value === "string") {
      console.log(
        "should create new company site: " +
          this.componentForm.get(this.COMPANY_SITE).value
      );
    } else {
      const myCompanySite = this.componentForm.controls[this.COMPANY_SITE]
        .value as CompanySite;
      const newRing = {
        primaryRing: true,
        locations: this.newLocations
          .filter(
            (myNewLocation) =>
              myNewLocation !== this.newLocations[0] && myNewLocation.selected
          )
          .map(
            (myNewLocation) =>
              ({
                latitude: myNewLocation.location.latitude,
                longitude: myNewLocation.location.longitude,
              } as Location)
          ),
      } as Ring;
      const newPolygon = {
        borderColor: "#00FFFF",
        fillColor: "#FFFFFF",
        latitude: this.newLocations[0].location.latitude,
        longitude: this.newLocations[0].location.longitude,
        title: this.componentForm.controls[this.PROPERTY].value,
        rings: [newRing],
      } as Polygon;
      myCompanySite.polygons.push(newPolygon);
      this.companySiteService
        .upsertCompanySite(myCompanySite)
        .pipe(takeUntilDestroyed(this.destroy))
        .subscribe((newCompanySite) => {
          this.componentForm.controls[this.COMPANY_SITE].setValue(
            newCompanySite
          );
          this.clearMapPins();
          this.updateMap(newCompanySite);
        });
    }
  }

  clearMapPins(): void {
    while (this.newLocations.length > 0) {
      this.newLocations.pop();
    }
    this.updateMapPushPins();
  }

  resetDb(): void {
    this.resetInProgress = true;
    this.companySiteService
      .resetDb()
      .pipe(
        switchMap(() =>
          this.companySiteService.findByTitleAndYear("Finkenwerder", 2020)
        ),
        filter(
          (myCompanySite) => myCompanySite?.length && myCompanySite?.length > 0
        ),
        takeUntilDestroyed(this.destroy)
      )
      .subscribe((companySite) => {
        this.componentForm.controls[this.COMPANY_SITE].setValue(companySite[0]);
        this.clearMapPins();
        this.updateMap(companySite[0]);
        this.resetInProgress = false;
      });
  }

  formatLabel(value: number): string {
    return "" + value;
  }

  displayTitle(companySite: CompanySite): string {
    return companySite && companySite.title ? companySite.title : "";
  }

  newLocationsValid(): boolean {
    return (
      this.newLocations.filter((nl) => nl.selected === true).length > 3 &&
      this.componentForm.valid
    );
  }

  private addPolygon(polygon: Polygon): void {
    //console.log(this.map.getCenter());
    const polygonRings = polygon.rings.map((myRing) =>
      myRing.locations.map(
        (myLocation) =>
          new Microsoft.Maps.Location(myLocation.latitude, myLocation.longitude)
      )
    );
    const mapPolygon = new Microsoft.Maps.Polygon(polygonRings);
    mapPolygon.metadata = {
      companySiteId: (
        this.componentForm.controls[this.COMPANY_SITE].value as CompanySite
      ).id,
      polygonId: polygon.id,
    } as PolygonMetaData;
    Microsoft.Maps.Events.addHandler(mapPolygon, "click", (e) =>
      this.onPolygonDblClick(e)
    );
    this.map.entities.push(mapPolygon);
  }

  private onPolygonDblClick(
    e:
      | Microsoft.Maps.IMouseEventArgs
      | Microsoft.Maps.IPrimitiveChangedEventArgs
  ): void {
	console.log(e);
    if (
      (e as Microsoft.Maps.IMouseEventArgs).targetType === "polygon" &&
      (e as Microsoft.Maps.IMouseEventArgs).eventName === "click"
    ) {
      //console.log((e as Microsoft.Maps.IMouseEventArgs).target);
      const myPolygon = (e as Microsoft.Maps.IMouseEventArgs)
        .target as Microsoft.Maps.Polygon;
      this.openDeleteDialog(myPolygon.metadata as PolygonMetaData);
    }
  }

  private onMapClick(
    e: Microsoft.Maps.IMouseEventArgs | Microsoft.Maps.IMapTypeChangeEventArgs
  ): void {
    if ((e as Microsoft.Maps.IMouseEventArgs).location) {
      const myLocation = {
        id: this.newLocations.length + 1,
        location: (e as Microsoft.Maps.IMouseEventArgs).location,
        selected: true,
      };
      this.newLocations.push(myLocation);
      this.map.entities.push(
        new Microsoft.Maps.Pushpin(myLocation.location, {
          title: "" + myLocation.id,
          icon: "assets/map-pin.png",
          anchor: new Microsoft.Maps.Point(12, 39),
        })
      );
    }
  }

  protected updateMapByCompanySite(): void {
    this.companySiteService
      .findByTitleAndYearWithChildren(
        this.getCompanySiteTitle(),
        this.componentForm.controls[this.SLIDER_YEAR].value as number
      )
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((companySite) => this.updateMap(companySite[0]));
  }

  private updateMap(companySite: CompanySite): void {
    if (this.map) {
      this.map.setOptions({
        center: new Microsoft.Maps.Location(
          companySite.polygons[0].latitude,
          companySite.polygons[0].longitude
        ),
      } as Microsoft.Maps.IMapLoadOptions);
      this.map.entities.clear();
      companySite.polygons.forEach((polygon) => this.addPolygon(polygon));
      this.map.setView({
        center: new Microsoft.Maps.Location(
          companySite.polygons[0].latitude,
          companySite.polygons[0].longitude
        ),
        zoom: 12,
      });
    }
  }

  private getCompanySiteTitle(): string {
    return typeof this.componentForm.get(this.COMPANY_SITE).value === "string"
      ? (this.componentForm.get(this.COMPANY_SITE).value as string)
      : (this.componentForm.get(this.COMPANY_SITE).value as CompanySite).title;
  }

  private updateMapPushPins(): void {
    const mapPinsToAdd: Microsoft.Maps.Pushpin[] = [];
    const mapPinsToRemove: Microsoft.Maps.Pushpin[] = [];
    const mapPins: Microsoft.Maps.Pushpin[] = [];
    for (let i = 0; i < this.map.entities.getLength(); i++) {
      if (
        typeof (this.map.entities.get(i) as Microsoft.Maps.Pushpin).getIcon ===
          "function" &&
        typeof (this.map.entities.get(i) as Microsoft.Maps.Pushpin).getTitle ===
          "function"
      ) {
        mapPins.push(this.map.entities.get(i) as Microsoft.Maps.Pushpin);
      }
    }
    if (this.newLocations.length === 0) {
      mapPins.forEach((myPin) => mapPinsToRemove.push(myPin));
    } else {
      this.newLocations.forEach((newLocation) => {
        const myMapPin = mapPins.filter(
          (mapPin) =>
            mapPin.getLocation().latitude === newLocation.location.latitude &&
            mapPin.getLocation().longitude === newLocation.location.longitude
        );
        if (!!myMapPin && myMapPin.length > 0 && !newLocation.selected) {
          mapPinsToRemove.push(myMapPin[0]);
        }
        if (!myMapPin || (myMapPin.length === 0 && newLocation.selected)) {
          mapPinsToAdd.push(
            new Microsoft.Maps.Pushpin(newLocation.location, {
              title: "" + newLocation.id,
              icon: "assets/map-pin.png",
              anchor: new Microsoft.Maps.Point(12, 39),
            })
          );
        }
      });
    }
    mapPinsToRemove.forEach((myPin) => this.map.entities.remove(myPin));
    mapPinsToAdd.forEach((myPin) => this.map.entities.add(myPin));
  }

  private openDeleteDialog(polygonMetaData: PolygonMetaData): void {
    const myPolygon = (
      this.componentForm.controls[this.COMPANY_SITE].value as CompanySite
    ).polygons.filter((polygon) => (polygon.id = polygonMetaData.polygonId));
    if (myPolygon && myPolygon.length > 0) {
      const dialogRef = this.dialog.open(PolygonDeleteDialogComponent, {
        width: "350px",
        data: {
          polygonName: myPolygon[0].title,
          polygonId: polygonMetaData.polygonId,
        } as DialogMetaData,
      });
      dialogRef.afterClosed().subscribe((result) => {
        console.log("The dialog was closed " + result);
        if (result === MyDialogResult.delete) {
          //console.log(polygonMetaData);
          this.companySiteService
            .deletePolygon(
              polygonMetaData.companySiteId,
              polygonMetaData.polygonId
            )
            .pipe(
              switchMap(() =>
                this.companySiteService.findById(polygonMetaData.companySiteId)
              ),
              takeUntilDestroyed(this.destroy)
            )
            .subscribe((myCompanySite) => {
              this.componentForm.controls[this.COMPANY_SITE].setValue(
                myCompanySite
              );
              this.clearMapPins();
              this.updateMap(myCompanySite);
            });
        }
      });
    }
  }
}
