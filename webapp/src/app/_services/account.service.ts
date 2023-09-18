import { Injectable } from "@angular/core";
import { User } from "../_model/user";
import { TokenStorageService } from "./token-storage.service";

@Injectable({
  providedIn: 'root',
})
export class AccountService {

  constructor(readonly tokenStorageService: TokenStorageService) {
  }


  public getUser(): User | null {
    return this.tokenStorageService.getUser();
  }
}
