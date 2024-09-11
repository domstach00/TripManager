import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { map, Observable, take } from 'rxjs';
import { Account } from "../_model/account";
import { Paths } from "../_model/paths";
import { AccountService } from "../_services/account.service";

@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate {
	constructor(
		private router: Router,
		private accountService: AccountService,
		) {
	}

	canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
		return this.accountService.currentAccount.pipe(
			map( (currentAccount: Account) => {
				if (!!currentAccount) {
					return true;
				} else {
					this.router.navigate([Paths.login])
					return false;
				}
			}),
			take(1),
		);
	}
}
