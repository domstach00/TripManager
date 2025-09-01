import { Component, Input, OnInit } from '@angular/core';
import { AccountService } from "../../../account/_serice/account.service";
import { Account } from "../../../account/_model/account";
import { TranslateService } from "@ngx-translate/core";

@Component({
    selector: 'app-default-page',
    templateUrl: './default-page.component.html',
    styleUrl: './default-page.component.scss',
    standalone: false
})
export class DefaultPageComponent implements OnInit {
	@Input() account?: Account;
	@Input() pageTitleKey?: string;

	constructor(
		readonly accountService: AccountService,
		protected translate: TranslateService,
	) {
	}

	ngOnInit() {
		if (!this.account) {
			this.accountService.currentAccount.subscribe(account =>
				this.account = account
			);
		}
	}

	getTitlePage(): string {
		return this.translate.instant(this.pageTitleKey);
	}

}
