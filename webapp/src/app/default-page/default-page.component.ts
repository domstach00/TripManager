import { Component, Input, OnInit } from '@angular/core';
import { Account } from "../_model/account";
import { AccountService } from "../_services/account.service";

@Component({
  selector: 'app-default-page',
  templateUrl: './default-page.component.html',
  styleUrl: './default-page.component.scss'
})
export class DefaultPageComponent implements OnInit {
	@Input() account?: Account;

	constructor(
		readonly accountService: AccountService,
	) {
	}

	ngOnInit() {
		if (!this.account) {
			this.accountService.currentAccount.subscribe(account =>
				this.account = account
			);
		}
	}


}
