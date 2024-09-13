import { ApiService } from "./api.service";
import { LoginCredentials } from "../_model/login-credentials";
import { Account } from "../_model/account";
import { ApiPath } from "../_model/ApiPath";
import { TokenStorageService } from "./token-storage.service";
import { RegisterCredentials } from "../_model/register-credentials";
import { ToastrService } from "ngx-toastr";
import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
import { Observable, ReplaySubject, Subject, take, tap } from "rxjs";
import { AccountService } from "./account.service";
import { RouterService } from "./router.service";

export const authJwtEnv = {
	config: {
		allowedDomains: ['localhost:4200', 'localhost:8080']
	}
}

@Injectable({
	providedIn: 'root',
})
export class AuthService {

	helper = new JwtHelperService();
	decodedToken: any;

	private currentAccountExistsSubject: Subject<boolean> = new ReplaySubject<boolean>(1)
	private currentAccountSubject: Subject<Account> = new ReplaySubject<Account>(1);
	private _currentAccount$: Observable<Account> = this.currentAccountSubject.asObservable();
	private _currentAccount: Account;

	constructor(
		readonly apiService: ApiService,
		readonly tokenStorageService: TokenStorageService,
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
		return this.apiService.post<Account>(ApiPath.login, loginCredentials).subscribe(user => {
			if (!!user) {
				this.tokenStorageService.saveToken(user.token)
				this.toastrService.success("Logged in")
				this._currentAccount$ = this.initCurrentAccount$();
				this.routerService.navToHome();
			} else {
				this.toastrService.error("Error")
			}
		}, error => this.toastrService.error(error))
	}

	public register(registerCredentials: RegisterCredentials) {
		this.apiService.post<boolean, RegisterCredentials>(ApiPath.register, registerCredentials).subscribe(isSuccess => {
			if (isSuccess) {
				this.toastrService.success("Account has been created")
				// TODO this.router.navigate(['/register-success'])
			} else
				this.toastrService.error("Error while creating account")
		}, error => {
			this.toastrService.error(error)
		})
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
