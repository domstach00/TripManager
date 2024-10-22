import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from "../_servive/auth.service";
import { Subscription } from "rxjs";
import { AccountService } from "../../_services/account.service";
import { RouterService } from "../../_services/router.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TranslateService } from "@ngx-translate/core";
import { Paths } from "../../_model/paths";
import { LoginCredentials } from "../_model/login-credentials";
import { ToastrService } from "ngx-toastr";


@Component({
	selector: 'app-login-form',
	templateUrl: './login-form.component.html',
	styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit, OnDestroy {
	protected subscribe = new Subscription();
	loginForm: FormGroup;
	isSubmitting: boolean = true;

	constructor(
		readonly accountService: AccountService,
		readonly authService: AuthService,
		readonly routerService: RouterService,
		private fb: FormBuilder,
		protected translate: TranslateService,
		readonly toastrService: ToastrService,
	) {
	}

	ngOnInit(): void {
		this.subscribe.add(
			this.authService.logout$().subscribe({
				next: () => { this.isSubmitting = false },
				error: () => { this.isSubmitting = false },
			})
		);

		this.loginForm = this.fb.group({
			email: ['', [Validators.required, Validators.email]],
			password: ['', [Validators.required]]
		});
	}


	onSubmit(): void {
		if (this.loginForm.valid) {
			this.loginForm.disable();
			this.isSubmitting = true;
			const loginCredentials: LoginCredentials = {
				email: this.loginForm.value['email'],
				password: this.loginForm.value['password']
			}

			this.subscribe.add(
				this.authService.login(loginCredentials).subscribe(_ => {
					this.loginForm.enable();
					this.isSubmitting = false;
					this.routerService.navToHome();
				}, error => {
					this.toastrService.error(error);
					this.loginForm.enable();
					this.isSubmitting = false;
				})
			);
		}
	}

	ngOnDestroy(): void {
		this.subscribe.unsubscribe();
	}

	protected readonly Paths = Paths;
}
