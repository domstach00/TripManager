import { Component, EventEmitter, Output } from '@angular/core';
import { SearchResultComponent } from "../../shared/directives/search-result/search-result.component";
import { Budget } from "../_model/budget";
import { TranslateService } from "@ngx-translate/core";
import { RouterService } from "../../shared/_service/router.service";
import { DateUtilService } from "../../shared/_service/date-util.service";
import { BudgetService } from "../_service/budget.service";
import { MatDialog } from "@angular/material/dialog";
import {
	ConfirmActionDialogComponent
} from "../../shared/_dialog/delete-confirmation-dialog/confirm-action-dialog.component";
import { BudgetCreateDialogComponent } from "../_dialog/budget-create-dialog/budget-create-dialog.component";

@Component({
    selector: 'budget-table',
    templateUrl: './budget-table.component.html',
    styleUrl: './budget-table.component.scss',
    standalone: false
})
export class BudgetTableComponent extends SearchResultComponent<Budget>{
	displayedColumns: string[] = ['name', 'owner', 'startDate', 'endDate', 'allocatedBudget', 'actions']
	@Output() refreshEvent = new EventEmitter<string>();

	constructor(
		protected override readonly translate: TranslateService,
		protected readonly routerService: RouterService,
		protected readonly dateUtilService: DateUtilService,
		readonly budgetService: BudgetService,
		readonly dialog: MatDialog
	) {
		super(translate);
	}

	isBudgetOwner(budget: Budget): boolean {
		return budget.owner?.id === this.currentAccountId;
	}

	isBudgetArchived(budget: Budget): boolean {
		return !!budget && budget.isArchived
	}

	archiveBudget(budgetId: string) {
		this.budgetService.archiveBudget(budgetId).subscribe(
			archivedBudget => {
				if (!!archivedBudget) {
					this.refreshEvent.emit();
				}
			}
		)
	}

	unArchiveBudget(budgetId: string) {
		this.budgetService.unArchiveBudget(budgetId).subscribe(
			archivedBudget => {
				if (!!archivedBudget) {
					this.refreshEvent.emit();
				}
			}
		)
	}

	editBudget(budget: Budget) {
		const dialogRef = this.dialog.open(BudgetCreateDialogComponent, {
			width: '400px',
			data: { budget: budget }
		});

		dialogRef.afterClosed().subscribe(updatedData => {
			if (updatedData) {
				this.budgetService.updateBudget(budget.id, updatedData).subscribe(updatedBudget => {
					if (updatedBudget) {
						this.refreshEvent.emit();
					}
				});
			}
		});
	}

	startNewBudgetPeriod(budgetId: string) {
		// TODO: Continue budget with new period and archive old budget if user wants it
	}

	leaveBudgetAsMember(budget: Budget) {
		const confirmationText = `Do you want to leave Budget ${budget.name}?`
		const dialogRef = this.dialog.open(ConfirmActionDialogComponent, {
			height: '300px',
			width: '600px',
			data: { elementName: budget.name, body: confirmationText, isWarning: true }
		})

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.budgetService.leaveBudget(budget.id).subscribe(_ => {
					this.refreshEvent.emit();
				})
			}
		})
	}

	deleteBudget(budget: Budget) {
		const confirmationText = `Do you want to delete Budget ${budget.name}?`
		const dialogRef = this.dialog.open(ConfirmActionDialogComponent, {
			height: '300px',
			width: '600px',
			data: { elementName: budget.name, body: confirmationText, isWarning: true }
		})

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.budgetService.deleteBudget(budget.id).subscribe(_ => {
					this.refreshEvent.emit();
				})
			}
		})
	}
}
