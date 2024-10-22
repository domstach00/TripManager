import { Component, Input } from "@angular/core";
import { Account } from "../../../_model/account";
import { AuthService } from "../../../_services/auth.service";
import { RouterService } from "../../../_services/router.service";


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
		readonly routerService: RouterService,
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
}
