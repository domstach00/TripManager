import { Injectable } from "@angular/core";
import { User } from "../_model/user";
import { AuthService } from "./auth.service";

@Injectable({
  providedIn: 'root',
})
export class AccountService {

  constructor(readonly authService: AuthService) {
  }


  public getUser(): User | null {
    return this.authService.getUser();
  }
}
