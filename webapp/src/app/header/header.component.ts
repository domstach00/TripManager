import { Component, OnInit } from '@angular/core';
import { Observable, Subscription } from "rxjs";
import { AuthService } from "../_services/auth.service";
import { Account } from "../_model/account";

@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
	isLoggedIn$: Observable<boolean>;

	currentAccount: Account;

	constructor(readonly authService: AuthService) {
		this.isLoggedIn$ = this.authService.isLoggedIn$;
		this.currentAccount = this.authService.currentAccount;
	}


	ngOnInit(): void {
	}

	logout() {
		this.authService.logout();
	}
}
