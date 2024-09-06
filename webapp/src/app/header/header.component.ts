import { Component, OnInit } from '@angular/core';
import { AccountService } from "../_services/account.service";
import { Subscription } from "rxjs";

@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
	private subscription = new Subscription();

	// currentAccount: Account;

	constructor(readonly accountService: AccountService) {
	}


	ngOnInit(): void {
		// this.accountService.currentAccount.subscribe(
		//   account => this.currentAccount = account
		// )
	}

	getUserName() {
		// return this.currentAccount?.username;
	}
}
