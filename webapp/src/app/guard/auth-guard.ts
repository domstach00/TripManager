import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';
import { catchError, map, Observable, of, take } from 'rxjs';
import { Account } from "../account/_model/account";
import { AccountService } from "../account/_serice/account.service";
import { RouterService } from "../shared/_service/router.service";

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
			catchError(_ => {
				this.routerService.navToLogin();
				return of(false)
			}),
			take(1),
		);
	}
}
