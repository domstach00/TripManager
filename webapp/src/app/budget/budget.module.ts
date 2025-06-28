import { NgModule } from "@angular/core";
import { SharedModule } from "../shared/shared.module";
import { BudgetComponent } from "./budget.component";
import { BudgetTablePresenterComponent } from "./budget-table/budget-table-presenter.component";
import { BudgetTableComponent } from "./budget-table/budget-table.component";
import { BudgetService } from "./_service/budget.service";
import { BudgetCreateDialogComponent } from "./_dialog/budget-create-dialog/budget-create-dialog.component";
import { BudgetRoutingModule } from "./budget-routing.module";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatNativeDateModule, MatOption } from "@angular/material/core";
import { BudgetDetailsComponent } from "./budget-details/budget-details.component";
import { MatProgressBar } from "@angular/material/progress-bar";
import { BudgetLayoutComponent } from "./budget-layout/budget-layout.component";
import {
	TransactionCreateDialogComponent
} from "./_dialog/transaction-create-dialog/transaction-create-dialog.component";
import { TransactionService } from "./_service/transaction.service";
import { MatSelect } from "@angular/material/select";
import { CategoryCreateDialogComponent } from "./_dialog/category-create-dialog/category-create-dialog.component";
import { NgxChartsModule } from '@swimlane/ngx-charts';
import {
	CategoryTableComponent
} from "./budget-details/category-table/category-table.component";
import {
	TransactionsOnCategoryTableComponent
} from "./transactions-on-category-table/transactions-on-category-table.component";
import {
	TransactionsOnCategorySearchableComponent
} from "./transactions-on-category-table/transactions-on-category-searchable.component";
import {
	SubcategoryCreateDialogComponent
} from "./_dialog/subcategory-create-dialog/subcategory-create-dialog.component";
import { CategoryTableV2Component } from "./budget-details/category-table-v2/category-table-v2.component";
import {
	SubcategoryTableComponent
} from "./budget-details/category-table-v2/subcategory-table/subcategory-table.component";
import { TransactionsSearchableComponent } from "./transactions-table/transactions-searchable.component";
import { TransactionsTableComponent } from "./transactions-table/transactions-table.component";
import { ExpenseDistributionChartComponent } from "./expense-distribution-chart/expense-distribution-chart.component";

@NgModule({
	declarations: [
		BudgetComponent,
		BudgetTablePresenterComponent,
		BudgetTableComponent,
		BudgetCreateDialogComponent,
		BudgetLayoutComponent,
		TransactionCreateDialogComponent,
		CategoryTableComponent,
		TransactionsOnCategorySearchableComponent,
		TransactionsOnCategoryTableComponent,
		ExpenseDistributionChartComponent,
		BudgetDetailsComponent,
	],
	imports: [
		SharedModule,
		BudgetRoutingModule,
		MatDatepickerModule,
		MatNativeDateModule,
		MatProgressBar,
		MatSelect,
		MatOption,
		NgxChartsModule,
		CategoryCreateDialogComponent,
		SubcategoryCreateDialogComponent,
		CategoryTableV2Component,
		SubcategoryTableComponent,
		TransactionsSearchableComponent,
		TransactionsTableComponent,
		// AppLoadingComponent,
	],
	providers: [
		BudgetService,
		TransactionService,
	],
	exports: [

	],
	bootstrap: [BudgetComponent]
})
export class BudgetModule {

}
