import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';
import { map, Observable, take } from 'rxjs';
import { Account } from "../_model/account";
import { AccountService } from "../_services/account.service";
import { RouterService } from "../_services/router.service";

@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate {
	constructor(
		private routerService: RouterService,
		private accountService: AccountService,
		) {
	}

	canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
		return this.accountService.currentAccount.pipe(
			map( (currentAccount: Account) => {
				if (!!currentAccount) {
					return true;
				} else {
					this.routerService.navToLogin();
					return false;
				}
			}),
			take(1),
		);
	}
}
