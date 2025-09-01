import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from "rxjs";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AccountService } from "../../account/_serice/account.service";
import { AuthService } from "../_servive/auth.service";
import { RouterService } from "../../shared/_service/router.service";
import { TranslateService } from "@ngx-translate/core";
import { ToastrService } from "ngx-toastr";
import { ForgotPassword } from "../_model/forgot-password";
import { Paths } from "../../shared/_model/paths";

@Component({
    selector: 'forgot-password',
    templateUrl: './forgot-password.component.html',
    styleUrl: './forgot-password.component.scss',
    standalone: false
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {
	protected subscribe = new Subscription();
	forgotPasswordForm: FormGroup;
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

		this.forgotPasswordForm = this.fb.group({
			email: ['', [Validators.required, Validators.email]],
		});

	}

	ngOnDestroy(): void {
		this.subscribe.unsubscribe();
	}

	onSubmit(): void {
		if (this.forgotPasswordForm.valid) {
			this.forgotPasswordForm.disable();
			this.isSubmitting = true;
			const forgotPassword: ForgotPassword = {
				email: this.forgotPasswordForm.value['email'],
			}

			this.subscribe.add(
				this.authService.forgotPassword(forgotPassword).subscribe({
					next: messageResponse => {
						this.toastrService.success(messageResponse.message);
						this.isSubmitting = false;
						this.forgotPasswordForm.enable()
						this.routerService.navToLogin();
					}, error: err => {
						console.error("Error while submitting forgotten password request", err)
						this.isSubmitting = false;
						this.forgotPasswordForm.enable()
					}
				})
			)
		}
	}

	protected readonly Paths = Paths;
}
