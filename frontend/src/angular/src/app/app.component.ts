import { ChangeDetectionStrategy, Component } from "@angular/core";

import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { RouterModule } from "@angular/router";

@Component({
    selector: "app-root",
    imports: [
      RouterModule,
        MatProgressSpinnerModule],
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent {
  protected title = "app";
}
