import { RouterModule, Routes } from "@angular/router";
import { BudgetComponent } from "./budget.component";
import { NgModule } from "@angular/core";

const budgetRoutes: Routes = [
	{ path: '', component: BudgetComponent },
]

@NgModule({
	imports: [RouterModule.forChild(budgetRoutes)],
	exports: [RouterModule]
})
export class BudgetRoutingModule {

}

