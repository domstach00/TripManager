import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from "../_services/auth.service";
import { Subscription } from "rxjs";
import { AccountService } from "../_services/account.service";
import { RouterService } from "../_services/router.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TranslateService } from "@ngx-translate/core";
import { Paths } from "../_model/paths";


@Component({
	selector: 'app-login-form',
	templateUrl: './login-form.component.html',
	styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit, OnDestroy {
	protected subscribe = new Subscription();
	loginForm: FormGroup;

	constructor(
		readonly accountService: AccountService,
		readonly authService: AuthService,
		readonly routerService: RouterService,
		private fb: FormBuilder,
		protected translate: TranslateService,
	) {
	}

	ngOnInit(): void {
		this.subscribe.add(
			this.accountService.currentAccount.subscribe(account => {
				if (!!account) {
					this.routerService.navToHome();
				}
			})
		);

		this.loginForm = this.fb.group({
			email: ['', [Validators.required, Validators.email]],
			password: ['', [Validators.required]]
		});
	}


	onSubmit(): void {
		if (this.loginForm.valid) {
			this.authService.login({email: this.loginForm.value['email'], password: this.loginForm.value['password']});
		}
	}

	ngOnDestroy(): void {
		this.subscribe.unsubscribe();
	}

	protected readonly Paths = Paths;
}
