import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators, ReactiveFormsModule } from "@angular/forms";
import { RouterService } from "../../shared/_service/router.service";
import { InitService } from "../_service/init.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { CommonModule } from "@angular/common";
import { AdminInitRequest } from "../_model/AdminInitRequest";
import { MessageResponse } from "../../shared/_model/base-models.interface";
import { SharedModule } from "../../shared/shared.module";

@Component({
	selector: 'app-init-admin',
	standalone: true,
	imports: [CommonModule, ReactiveFormsModule, SharedModule],
	templateUrl: './init-admin.component.html',
	styleUrl: './init-admin.component.scss'
})
export class InitAdminComponent implements OnInit {
	enabled = false;
	submitting = false;
	error = '';

	form!: FormGroup;

	constructor(
		private fb: FormBuilder,
		private initService: InitService,
		private routerService: RouterService,
		private snackBar: MatSnackBar
	) {}

	ngOnInit(): void {
		this.form = this.fb.group(
			{
				token: [''],
				email: ['', [Validators.required, Validators.email]],
				username: ['', [Validators.required, Validators.maxLength(20)]],
				password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(120)]],
				confirmPassword: ['', [Validators.required]],
				sendActivationEmail: [false],
			},
			{ validators: [this.passwordsMatchValidator] }
		);

		this.initService.checkEnabled().subscribe({
			next: enabled => this.enabled = enabled,
			error: () => {
				this.enabled = false;
				this.routerService.navToLogin();
				this.snackBar.open('Initialize module is not permitted', 'Close', { duration: 3000 });
			}
		});
	}

	get f() { return this.form.controls; }
	get passwordsMismatch() { return this.form.hasError('passwordsMismatch'); }

	private passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
		const pwd  = control.get('password')?.value;
		const conf = control.get('confirmPassword')?.value;
		if (!pwd || !conf) return null;
		return pwd === conf ? null : { passwordsMismatch: true };
	}

	submit() {
		if (this.form.invalid || this.passwordsMismatch) {
			this.form.markAllAsTouched();
			return;
		}

		this.submitting = true;
		this.error = '';

		const adminInitRequest: AdminInitRequest = {
			email: this.f['email'].value,
			username: this.f['username'].value,
			password: this.f['password'].value,
			sendActivationEmail: this.f['sendActivationEmail'].value,
		};

		const initTokenValue: string = this.f['token'].value ?? '';

		this.initService.createAdmin(adminInitRequest, initTokenValue).subscribe({
			next: (resp: MessageResponse) => {
				const msg: string = resp?.message
				this.snackBar.open(msg || 'Admin created. App will restart.', 'Close', { duration: 6000 });
				this.routerService.navToLogin();
			},
			error: (e) => {
				this.error = e?.error || 'Initialization failed';
				this.snackBar.open('Initialization failed', 'Close', { duration: 3000 });
				this.submitting = false;
			}
		});
	}
}
