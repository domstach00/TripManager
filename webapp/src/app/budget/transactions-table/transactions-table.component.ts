import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SearchResultComponent } from "../../shared/directives/search-result/search-result.component";
import { Transaction } from "../_model/transaction";

@Component({
  selector: 'transactions-table',
  templateUrl: './transactions-table.component.html',
  styleUrl: './transactions-table.component.scss'
})
export class TransactionsTableComponent extends SearchResultComponent<Transaction> {
	@Input() displayHeaders: boolean = true;
	displayedColumns: string[] = ['description', 'amount', 'transactionDate', 'actions'];
	@Output() refreshEvent = new EventEmitter();


	editTransaction(transaction: Transaction) {
		// TODO: Edit transactions
	}

	deleteTransaction(transaction: Transaction) {
		// TODO: Delete transactions
	}
}
