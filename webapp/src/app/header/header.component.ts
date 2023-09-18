import { Component } from '@angular/core';
import { AccountService } from "../_services/account.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  constructor(readonly accountService: AccountService) {
  }


  getUserName() {
    return this.accountService.getUser()?.username
  }
}
