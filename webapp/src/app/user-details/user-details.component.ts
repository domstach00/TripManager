import { Component } from '@angular/core';
import { AccountService } from "../_services/account.service";

@Component({
	selector: 'app-user-details',
	templateUrl: './user-details.component.html',
	styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent {
	constructor(readonly accountService: AccountService) {

	}


}
