import { Component, OnInit } from '@angular/core';
import { Transaction } from "../_model/transaction";
import { animate, state, style, transition, trigger } from "@angular/animations";
import { SearchResultComponent } from "../../shared/directives/search-result/search-result.component";

interface TransactionGroup {
	subCategoryId: string;
	transactions: Transaction[];
}


@Component({
    selector: 'transactions-on-category-table',
    templateUrl: './transactions-on-category-table.component.html',
    styleUrl: './transactions-on-category-table.component.scss',
    animations: [
        trigger('detailExpand', [
            state('collapsed', style({ height: '0px', minHeight: '0', visibility: 'hidden' })),
            state('expanded', style({ height: '*', visibility: 'visible' })),
            transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
        ]),
    ],
    standalone: false
})
export class TransactionsOnCategoryTableComponent extends SearchResultComponent<Transaction> implements OnInit {
	groupedTransactions: TransactionGroup[] = [];
	noSubCategoryTransactions: Transaction[] = [];
	expandedGroupIds: Set<string> = new Set();

	transactionDisplayedColumns: string[] = ['description', 'amount', 'transactionDate'];

	override ngOnInit(): void {
		super.ngOnInit();
		this.groupTransactions();
	}

	private groupTransactions(): void {
		const groupMap: { [key: string]: Transaction[] } = {};
		this.noSubCategoryTransactions = [];

		this.dataSet.forEach(transaction => {
			if (transaction.subCategoryId) {
				if (!groupMap[transaction.subCategoryId]) {
					groupMap[transaction.subCategoryId] = [];
				}
				groupMap[transaction.subCategoryId].push(transaction);
			} else {
				this.noSubCategoryTransactions.push(transaction);
			}
		});

		this.groupedTransactions = Object.keys(groupMap).map(subCatId => ({
			subCategoryId: subCatId,
			transactions: groupMap[subCatId]
		}));
	}

	toggleGroup(group: TransactionGroup): void {
		if (this.expandedGroupIds.has(group.subCategoryId)) {
			this.expandedGroupIds.delete(group.subCategoryId);
		} else {
			this.expandedGroupIds.add(group.subCategoryId);
		}
	}

	isGroupExpanded(group: TransactionGroup): boolean {
		return this.expandedGroupIds.has(group.subCategoryId);
	}

	isGroupExpandedRow = (index: number, row: any): boolean => {
		return this.isGroupExpanded(row);
	}
}
