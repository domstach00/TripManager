import { RouterModule, Routes } from "@angular/router";
import { BudgetComponent } from "./budget.component";
import { NgModule } from "@angular/core";
import { BudgetDetailsComponent } from "./budget-details/budget-details.component";
import { BudgetLayoutComponent } from "./budget-layout/budget-layout.component";

const budgetRoutes: Routes = [
	{
		path: '',
		component: BudgetLayoutComponent,
		children: [
			{ path: '', component: BudgetComponent },
			{ path: ':id', component: BudgetDetailsComponent }, // TODO: Add BudgetAccessGuard
		]
	}
	// { path: '', component: BudgetComponent },
	// { path: ':id', component: BudgetDetailsComponent }, // TODO: Add BudgetAccessGuard
]

@NgModule({
	imports: [RouterModule.forChild(budgetRoutes)],
	exports: [RouterModule]
})
export class BudgetRoutingModule {

}

