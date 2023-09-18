import { Injectable } from "@angular/core";
import { User } from "../_model/user";

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {
  private readonly tokenLocation: Storage = window.localStorage;
  constructor() {
  }

  signOut(): void {
    this.tokenLocation.clear();
  }

  public saveToken(token: string): void {
    this.tokenLocation.removeItem(TOKEN_KEY);
    this.tokenLocation.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return this.tokenLocation.getItem(TOKEN_KEY);
  }

  public saveUser(user: User): void {
    this.tokenLocation.removeItem(USER_KEY);
    this.tokenLocation.setItem(USER_KEY, JSON.stringify(user))
  }

  public getUser(): User | null {
    const user = this.tokenLocation.getItem(USER_KEY);
    if (!!user) {
      return JSON.parse(user)
    }
    return null;
  }
}
