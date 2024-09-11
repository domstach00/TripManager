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

	constructor(readonly authService: AuthService) {
	}

	get currentAccount() {
		return this.authService.currentAccount;
	}

	getName(): string {
		return this.currentAccount?.username ?? ''
	}

	ngOnInit(): void {
	}

	logout() {
		this.authService.logout();
	}
}
