import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../_servive/auth.service";
import { Paths } from "../../shared/_model/paths";
import { TranslateService } from "@ngx-translate/core";
import { ToastrService } from "ngx-toastr";
import { RouterService } from "../../shared/_service/router.service";
import { RegisterCredentials } from "../_model/register-credentials";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrl: './register.component.scss',
    standalone: false
})
export class RegisterComponent implements OnInit {
	protected readonly MIN_PASSWORD_LENGTH: number = 6;
	protected readonly MAX_PASSWORD_LENGTH: number = 50
	/**
	 * Minimum 6 characters
	 * Maximum 50 characters,
	 * At least 1 UPPERCASE letter,
	 * At least 1 lowercase letter,
	 * At lest 1 number
	 * At lest 1 special character
	 * */
	public readonly PASSWORD_REGEX: RegExp = new RegExp(`^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d!@#$%^&*]{${this.MIN_PASSWORD_LENGTH},${this.MAX_PASSWORD_LENGTH}`);
	protected readonly PASSWORD_UPPERCASE_LETTERS: RegExp = new RegExp(/[A-Z]/);
	protected readonly PASSWORD_LOWERCASE_LETTERS: RegExp = new RegExp(/[a-z]/);
	protected readonly PASSWORD_NUMBER_CHARACTERS: RegExp = new RegExp(/\d/);
	protected readonly PASSWORD_SPECIAL_CHARACTERS: RegExp = new RegExp(/[!@#$%^&*]/);

	registerForm: FormGroup;
	isSubmitting: boolean = false;

	constructor(
		private fb: FormBuilder,
		private authService: AuthService,
		protected translate: TranslateService,
		readonly routerService: RouterService,
		readonly toastrService: ToastrService,
	) {}

	ngOnInit(): void {
		this.registerForm = this.fb.group({
			name: ['', [Validators.required]],
			email: ['', [Validators.required, Validators.email]],
			password: ['', [Validators.required, Validators.min(this.MIN_PASSWORD_LENGTH), Validators.max(this.MAX_PASSWORD_LENGTH), Validators.pattern(this.PASSWORD_UPPERCASE_LETTERS), Validators.pattern(this.PASSWORD_LOWERCASE_LETTERS), Validators.pattern(this.PASSWORD_NUMBER_CHARACTERS), Validators.pattern(this.PASSWORD_SPECIAL_CHARACTERS)]],
			passwordConfirmation: ['', [Validators.required]]
		});
	}

	isEmailInvalid(): boolean {
		return !!this.registerForm.value['email']
			&& this.registerForm.get('email').hasError('email');
	}

	isPasswordInvalid(): boolean {
		return !!this.registerForm.value['password']
			&& !this.registerForm.get('password').valid;
	}

	isPasswordValidationMinValid(): boolean {
		return this.registerForm.value['password']?.length >= this.MIN_PASSWORD_LENGTH;
	}

	isPasswordValidationMaxValid(): boolean {
		return this.registerForm.value['password']?.length <= this.MAX_PASSWORD_LENGTH;
	}


	isPasswordValidationUpperCaseCountValid(): boolean {
		return this.PASSWORD_UPPERCASE_LETTERS.test(this.registerForm.value['password'] ?? '');
	}

	isPasswordValidationLowerCaseCountValid(): boolean {
		return this.PASSWORD_LOWERCASE_LETTERS.test(this.registerForm.value['password'] ?? '');
	}

	isPasswordValidationNumberCountValid(): boolean {
		return this.PASSWORD_NUMBER_CHARACTERS.test(this.registerForm.value['password'] ?? '');
	}

	isPasswordValidationSpecialCharCountValid(): boolean {
		return this.PASSWORD_SPECIAL_CHARACTERS.test(this.registerForm.value['password'] ?? '');
	}


	isPasswordConfirmationInvalid(): boolean {
		return !!this.registerForm.value['passwordConfirmation']
			&& this.registerForm.value['passwordConfirmation'] !== this.registerForm.value['password'];
	}

	onSubmit(): void {
		if (this.registerForm.valid) {
			this.registerForm.disable();
			this.isSubmitting = true;
			const registerCredentials: RegisterCredentials = {
				email: this.registerForm.value['email'],
				name: this.registerForm.value['name'],
				password: this.registerForm.value['password']
			}

			this.authService.register(registerCredentials).subscribe(isSuccess => {
				this.isSubmitting = false;
				if (isSuccess) {
					this.toastrService.success("Account has been created")
					this.routerService.navToRegisterSuccess();
				} else {
					this.toastrService.error("Error while creating account")
					this.registerForm.enable();
				}
			}, error => {
				this.isSubmitting = false;
				this.toastrService.error(error)
				this.registerForm.enable();
			});


		}
	}

	protected readonly Paths = Paths;
}
