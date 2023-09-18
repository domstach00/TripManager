import { ApiService } from "./api.service";
import { LoginCredentials } from "../_model/login-credentials";
import { User } from "../_model/user";
import { ApiPath } from "../_model/ApiPath";
import { TokenStorageService } from "./token-storage.service";
import { RegisterCredentials } from "../_model/register-credentials";
import { ToastrService } from "ngx-toastr";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    readonly apiService: ApiService,
    readonly tokenStorageService: TokenStorageService,
    readonly toastrService: ToastrService,
    readonly router: Router,
  ) {
  }

  public login(loginCredentials: LoginCredentials) {
    return this.apiService.post<User>(ApiPath.login, loginCredentials).subscribe(user => {
      if (!!user) {
        this.tokenStorageService.saveToken(user.token)
        this.tokenStorageService.saveUser(user);
        this.toastrService.success("Logged in")
        console.log("ez")
        this.router.navigate(['/home'])
      } else {
        this.toastrService.error("Error")
      }
    }, error => this.toastrService.error(error))
  }

  public register(registerCredentials: RegisterCredentials) {
    this.apiService.post<boolean, RegisterCredentials>(ApiPath.register, registerCredentials).subscribe(isSuccess => {
      if (isSuccess) {
        this.toastrService.success("Account has been created")
        this.router.navigate(['/register-success'])
      } else
        this.toastrService.error("Error while creating account")
    },error => {
      this.toastrService.error(error)
    })
  }

  public isLoggedIn(): boolean {
    return !!this.tokenStorageService.getUser()
  }
}
