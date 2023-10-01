import { ApiService } from "./api.service";
import { LoginCredentials } from "../_model/login-credentials";
import { User } from "../_model/user";
import { ApiPath } from "../_model/ApiPath";
import { TokenStorageService } from "./token-storage.service";
import { RegisterCredentials } from "../_model/register-credentials";
import { ToastrService } from "ngx-toastr";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { JwtHelperService } from "@auth0/angular-jwt";

export const authJwtEnv = {
  config: {
    allowedDomains: ['localhost:4200', 'localhost:8080']
  }
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  helper = new JwtHelperService();
  decodedToken: any;
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
        this.toastrService.success("Logged in")
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
    const x = this.tokenStorageService.getToken();
    if (!!x)
      console.log(this.helper.decodeToken(x))
    return !this.helper.isTokenExpired(this.tokenStorageService.getToken());
  }

  public loggedIn() {
    const token = this.tokenStorageService.getToken();
    return !this.helper.isTokenExpired(token);
  }

  public logout() {
    this.tokenStorageService.logout();
  }

  public getUser(): User | null {
    const token = this.tokenStorageService.getToken();
    return !!token ? this.helper.decodeToken<User>(token) : null;
  }
}
