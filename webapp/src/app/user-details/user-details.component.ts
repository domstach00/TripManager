import { Component } from '@angular/core';
import { AccountService } from "../account/_serice/account.service";

@Component({
    selector: 'app-user-details',
    templateUrl: './user-details.component.html',
    styleUrls: ['./user-details.component.scss'],
    standalone: false
})
export class UserDetailsComponent {
	constructor(readonly accountService: AccountService) {

	}


}
