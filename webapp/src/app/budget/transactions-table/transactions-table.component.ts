import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SearchResultComponent } from "../../shared/directives/search-result/search-result.component";
import { Transaction } from "../_model/transaction";
import { CommonModule } from "@angular/common";
import { MatTableModule } from "@angular/material/table";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MatMenuModule } from "@angular/material/menu";
import { TranslateModule } from "@ngx-translate/core";
import { MatSortModule } from "@angular/material/sort";

@Component({
  selector: 'transactions-table',
  templateUrl: './transactions-table.component.html',
  styleUrl: './transactions-table.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    TranslateModule,
    MatSortModule
  ]
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
