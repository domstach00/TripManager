import { Component, Input } from '@angular/core';
import { AuthService } from "../_services/auth.service";
import { Account } from "../_model/account";
import { Paths } from "../_model/paths";
import { Router } from "@angular/router";

@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
	@Input() account: Account;
	isAccMenuOpened: boolean = false;

	constructor(
		readonly authService: AuthService,
		readonly router: Router,
	) {
	}

	getName(): string {
		return this.account.username;
	}

	onMenuStatusChange() {
		this.isAccMenuOpened = !this.isAccMenuOpened;
	}

	logout() {
		this.authService.logout();
	}

	private navTo(path: Paths) {
		this.router.navigate([path]);
	}

	navToSettings() {
		this.navTo(Paths.settings);
	}

	navToHome() {
		this.navTo(Paths.home);
	}

	navToTrips() {
		this.navTo(Paths.trips)
	}
}
