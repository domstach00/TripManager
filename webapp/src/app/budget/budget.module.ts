import { NgModule } from "@angular/core";
import { SharedModule } from "../shared/shared.module";
import { BudgetComponent } from "./budget.component";
import { BudgetRoutingModule } from "./budget-routing.module";

@NgModule({
	declarations: [
		BudgetComponent,
	],
	imports: [
		SharedModule,
		BudgetRoutingModule
	],
	providers: [

	],
	exports: [

	],
	bootstrap: [BudgetComponent]
})
export class BudgetModule {

}
