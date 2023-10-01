import { Injectable } from "@angular/core";

const TOKEN_KEY = 'auth-token';

export function tokenGetter() {
  return window.localStorage.getItem(TOKEN_KEY);
}

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
    console.log('save')
    this.tokenLocation.removeItem(TOKEN_KEY);
    this.tokenLocation.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return tokenGetter();
  }

  public logout() {
    this.tokenLocation.removeItem(TOKEN_KEY);
  }

}
