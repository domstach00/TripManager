import { Injectable } from "@angular/core";
import jwtDecode, { JwtPayload } from "jwt-decode";

const TOKEN_KEY = 'auth-token';
const DEFAULT_COOKIE_TIME =  60 * 60 * 1000;

export function tokenGetter() {
  return getCookie(TOKEN_KEY);
}

export function setCookie(name: string, value: string, timeMs: number | undefined) {
  const date = new Date();
  timeMs = !!timeMs ? timeMs : new Date().getTime() + DEFAULT_COOKIE_TIME;

  date.setTime(timeMs);

  document.cookie = name+"="+value+"; expires="+date.toUTCString()+"; path=/";
}

export function getCookie(name: string): string | null {
  const value = "; " + document.cookie;
  const parts = value.split("; " + name + "=");

  if (parts.length == 2) {
    let result = parts.pop()?.split(";")?.shift()
    return !!result ? result : null;
  }
  return null;
}

export function deleteCookie(name: string) {
  const date = new Date();

  date.setTime(date.getTime() + (-1 * 24 * 60 * 60 * 1000));

  document.cookie = name+"=; expires="+date.toUTCString()+"; path=/";
}

@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {
  constructor() {
  }

  public saveToken(token: string): void {
    let expToken = jwtDecode<JwtPayload>(token).exp;
    if (!!expToken)
      expToken = expToken * 1000;
    setCookie(TOKEN_KEY, token, expToken)
  }

  public getToken(): string | null {
    return tokenGetter();
  }

  public logout() {
    deleteCookie(TOKEN_KEY);
  }
}
