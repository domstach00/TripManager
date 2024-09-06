import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from "../_services/auth.service";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";

@Component({
	selector: 'app-logout',
	templateUrl: './logout.component.html',
	styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit, OnDestroy {
	protected subscription = new Subscription()

	constructor(
		readonly authService: AuthService,
		readonly router: Router
	) {
	}

	ngOnInit(): void {
		this.subscription.add(
			this.authService.isLoggedIn$.subscribe(isLogged => {
				if (isLogged) {
					this.router.navigate(['/home']);
				} else {
					this.authService.logout();
					this.router.navigate(['/login'])
				}
			})
		);
	}

	ngOnDestroy(): void {
		this.subscription.unsubscribe();
	}
}
