import { Component, OnInit } from '@angular/core';
import { SearchableComponent } from "../../shared/directives/search/searchable.component";
import { Budget } from "../_model/budget";
import { Page } from "../../shared/_model/base-models.interface";
import { ApiParams } from "../../shared/directives/search/searchable.util";
import { Observable } from "rxjs";
import { BudgetService } from "../_service/budget.service";
import { AccountService } from "../../account/_serice/account.service";
import { ActivatedRoute } from "@angular/router";
import { BudgetCreateDialogComponent } from "../_dialog/budget-create-dialog/budget-create-dialog.component";
import { MatDialog } from "@angular/material/dialog";

@Component({
    selector: 'budget-table-presenter',
    templateUrl: './budget-table-presenter.component.html',
    styleUrl: './budget-table-presenter.component.scss',
    standalone: false
})
export class BudgetTablePresenterComponent extends SearchableComponent<Budget, Page<Budget>> implements OnInit {
	joinCode: string = '';

	constructor(
		override readonly accountService: AccountService,
		override readonly activatedRoute: ActivatedRoute,
		readonly budgetService: BudgetService,
		readonly dialog: MatDialog
	) {
		super(accountService, activatedRoute);
	}

	doSearch(options: ApiParams): Observable<Page<Budget>> {
		return this.budgetService.getBudgets(options);
	}

	openCreateBudgetDialog(): void {
		const dialogRef = this.dialog.open(BudgetCreateDialogComponent, {
			width: '400px',
			data: {}
		});

		dialogRef.afterClosed().subscribe(budgetToCreate => {
			if (budgetToCreate) {
				this.budgetService.postBudget(budgetToCreate).subscribe(budget => {
					if (!!budget) {
						this.prepareQueryParamsAndSearch();
					}
				});
			}
		});
	}

	joinBudget() {
		// TODO: Ability to join budget via code
	}
}
