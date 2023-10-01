import { Component, OnInit } from '@angular/core';
import { AuthService } from "../_services/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

  constructor(
    readonly authService: AuthService,
    readonly router: Router
  ) {
  }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'])
    } else {
      this.authService.logout();
      this.router.navigate(['/login'])
    }
  }


}
