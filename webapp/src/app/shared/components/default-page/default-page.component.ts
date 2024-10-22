import { Component, Input, OnInit } from '@angular/core';
import { AccountService } from "../../../account/_serice/account.service";
import { Account } from "../../../account/_model/account";

@Component({
  selector: 'app-default-page',
  templateUrl: './default-page.component.html',
  styleUrl: './default-page.component.scss'
})
export class DefaultPageComponent implements OnInit {
	@Input() account?: Account;
	@Input() pageTitleKey?: string;

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
