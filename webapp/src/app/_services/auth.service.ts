import { ApiService } from "./api.service";
import { LoginCredentials } from "../_model/login-credentials";
import { Account } from "../_model/account";
import { ApiPath } from "../_model/ApiPath";
import { TokenStorageService } from "./token-storage.service";
import { RegisterCredentials } from "../_model/register-credentials";
import { ToastrService } from "ngx-toastr";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { JwtHelperService } from "@auth0/angular-jwt";
import { Observable, ReplaySubject, Subject, take } from "rxjs";
import { AccountService } from "./account.service";

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
		readonly router: Router,
		readonly accountService: AccountService,
	) {
		this._currentAccount = <Account><unknown>undefined
	}

	get currentAccount(): Account {
		return <Account>this._currentAccount;
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
				this.router.navigate(['/home'])
			} else {
				this.toastrService.error("Error")
			}
		}, error => this.toastrService.error(error))
	}

	public register(registerCredentials: RegisterCredentials) {
		this.apiService.post<boolean, RegisterCredentials>(ApiPath.register, registerCredentials).subscribe(isSuccess => {
			if (isSuccess) {
				this.toastrService.success("Account has been created")
				this.router.navigate(['/register-success'])
			} else
				this.toastrService.error("Error while creating account")
		}, error => {
			this.toastrService.error(error)
		})
	}

	public isLoggedIn(): boolean {
		return !this.helper.isTokenExpired(this.tokenStorageService.getToken());
	}

	public loggedIn() {
		const token = this.tokenStorageService.getToken();
		return !this.helper.isTokenExpired(token);
	}

	public logout() {
		this.tokenStorageService.logout();
	}
}
