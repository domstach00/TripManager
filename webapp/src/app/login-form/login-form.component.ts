import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { AuthService } from "../_services/auth.service";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { AccountService } from "../_services/account.service";
import { RouterService } from "../_services/router.service";

enum OpenForm {
	None,
	Login,
	Register
}

@Component({
	selector: 'app-login-form',
	templateUrl: './login-form.component.html',
	styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit, OnDestroy {
	protected subscribe = new Subscription();
	@Output() onSubmitLoginEvent = new EventEmitter();
	@Output() onSubmitRegisterEvent = new EventEmitter();
	active: OpenForm = OpenForm.Login;
	name: string = "";
	email: string = "";
	password: string = "";
	passwordConfirm: string = "";
	OPEN_FORM = OpenForm;

	constructor(
		readonly accountService: AccountService,
		readonly authService: AuthService,
		readonly routerService: RouterService,
		readonly toastrService: ToastrService
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
	}

	onLoginTab(): void {
		this.active = OpenForm.Login;
	}

	onRegisterTab(): void {
		this.active = OpenForm.Register;
	}

	onSubmitLogin(): void {
		this.authService.login({email: this.email, password: this.password})
	}

	onSubmitRegister(): void {
		if (this.password !== this.passwordConfirm) {
			this.toastrService.error("Passwords are different")
			return;
		}
		this.authService.register({email: this.email, name: this.name, password: this.password})
	}

	ngOnDestroy(): void {
		this.subscribe.unsubscribe();
	}

}
