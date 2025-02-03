import { NgModule } from "@angular/core";
import { SharedModule } from "../shared/shared.module";
import { BudgetComponent } from "./budget.component";
import { BudgetTablePresenterComponent } from "./budget-table/budget-table-presenter.component";
import { BudgetTableComponent } from "./budget-table/budget-table.component";
import { BudgetService } from "./_service/budget.service";
import { BudgetCreateDialogComponent } from "./_dialog/budget-create-dialog/budget-create-dialog.component";
import { BudgetRoutingModule } from "./budget-routing.module";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatNativeDateModule } from "@angular/material/core";

@NgModule({
	declarations: [
		BudgetComponent,
		BudgetTablePresenterComponent,
		BudgetTableComponent,
		BudgetCreateDialogComponent,
	],
	imports: [
		SharedModule,
		BudgetRoutingModule,
		MatDatepickerModule,
		MatNativeDateModule,
	],
	providers: [
		BudgetService,
	],
	exports: [

	],
	bootstrap: [BudgetComponent]
})
export class BudgetModule {

}
