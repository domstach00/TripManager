import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouterService } from '../../shared/_service/router.service';
import { AuthService } from "../_servive/auth.service";
import { Paths } from "../../shared/_model/paths";

@Component({
	selector: 'app-activate-account',
	templateUrl: './activate-account.component.html',
	styleUrls: ['./activate-account.component.scss']
})
export class ActivateAccountComponent implements OnInit {
	successMessageKey: string | null = null;
	errorMessageKey: string | null = null;
	isLoading: boolean = true;

	constructor(
		private route: ActivatedRoute,
		private routerService: RouterService,
		private authService: AuthService
	) {}

	ngOnInit(): void {
		this.route.queryParams.subscribe(params => {
			const token = params['token'];

			if (!token) {
				this.isLoading = false;
				this.errorMessageKey = 'Brak tokena aktywacyjnego w URL.';
				return;
			}

			this.activateAccount(token);
		});
	}

	private activateAccount(token: string): void {
		this.authService.activateAccount(token).subscribe({
			next: _ => {
				this.isLoading = false;
				this.successMessageKey = 'activate.account.success';
			},
			error: _ => {
				this.isLoading = false;
				this.errorMessageKey = 'activate.account.error';
			}
		});
	}

	protected readonly Paths = Paths;
}
