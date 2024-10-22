import { ApiService } from "../../_services/api.service";
import { LoginCredentials } from "../_model/login-credentials";
import { Account } from "../../_model/account";
import { ApiPath } from "../../_model/ApiPath";
import { RegisterCredentials } from "../_model/register-credentials";
import { ToastrService } from "ngx-toastr";
import { Injectable } from "@angular/core";
import { Observable, ReplaySubject, Subject, take, tap } from "rxjs";
import { AccountService } from "../../_services/account.service";
import { RouterService } from "../../_services/router.service";

@Injectable({
	providedIn: 'root',
})
export class AuthService {
	private currentAccountExistsSubject: Subject<boolean> = new ReplaySubject<boolean>(1)
	private currentAccountSubject: Subject<Account> = new ReplaySubject<Account>(1);
	private _currentAccount$: Observable<Account> = this.currentAccountSubject.asObservable();
	private _currentAccount: Account;

	constructor(
		readonly apiService: ApiService,
		readonly toastrService: ToastrService,
		readonly routerService: RouterService,
		readonly accountService: AccountService,
	) {
		this._currentAccount = <Account><unknown>undefined
	}

	get currentAccount(): Account {
		return this._currentAccount;
	}

	get currentAccount$(): Observable<Account> {
		return this._currentAccount$;
	}

	set currentAccount(account: Account) {
		this._currentAccount = account;
		this.currentAccountSubject.next(this._currentAccount);
		this.currentAccountExistsSubject.next(!!this._currentAccount)
	}

	initCurrentAccount$(): Observable<Account> {
		if (!this._currentAccount) {
			let observable$: Observable<Account>;
			observable$ = this.accountService.getCurrentAccount();
			observable$.pipe(take(1)).subscribe({
				next: currentAccount => this.currentAccount = currentAccount,
				// error: () => this.currentAccount = null;
			})
		}
		return this._currentAccount$;
	}

	public login(loginCredentials: LoginCredentials) {
		return this.apiService.post<string>(ApiPath.login, loginCredentials);
	}

	public register(registerCredentials: RegisterCredentials) {
		return this.apiService.post<boolean, RegisterCredentials>(ApiPath.register, registerCredentials);
	}

	get isLoggedIn$(): Observable<boolean> {
		return this.currentAccountExistsSubject.asObservable();
	}

	public logout() {
		this.logout$().subscribe({
			next: () => {
				this.redirectToLoginPage();
			},
			error: () => {
				console.log('[WARN] Error when logging out')
			}
		})
	}

	public redirectToLoginPage() {
		this.routerService.navToLogin();
	}

	public logout$(): Observable<any> {
		return this.apiService.get<string>(ApiPath.logout, {}).pipe(
			take(1),
			tap((responseMessage) => {
				if (!!responseMessage) {
					console.log(responseMessage)
				} else {
					console.log('Logout response received with no user response message.')
				}
			})
		)
	}
}
