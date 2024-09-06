import { Injectable } from "@angular/core";
import { Account } from "../_model/account";
import { AuthService } from "./auth.service";
import { Observable, publishReplay, refCount, take, tap } from "rxjs";
import { ApiService } from "./api.service";
import { ApiPath } from "../_model/ApiPath";

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private static readonly ACC_CACHE_EXPIRE = 1000; // one second
  private currentAccountCacheTimestamp = 0;
  private currentAccountCache$: Observable<Account>;

  constructor(
    readonly authService: AuthService,
    readonly apiService: ApiService,
  ) {
    this.currentAccountCache$ = this.getCurrentAccount();
  }


  public getUser(): Account | null {
    return this.authService.getUser();
  }

  get currentAccount(): Observable<Account> {
    if (this.isCacheInvalid(AccountService.ACC_CACHE_EXPIRE, this.currentAccountCacheTimestamp)) {
      this.currentAccountCache$ = this.getCurrentAccount()
        .pipe(
          tap(() => {
            this.currentAccountCacheTimestamp = Date.now();
          }),
          publishReplay(AccountService.ACC_CACHE_EXPIRE),
          refCount(),
          take(1),
        );
    }
    return this.currentAccountCache$;
  }

  public getCurrentAccount(): Observable<Account> {
    return this.apiService.get(ApiPath.currentAccount)
  }

  private isCacheInvalid(maxCacheTime: number, currentCacheTime: number): boolean {
    return Date.now() - currentCacheTime > maxCacheTime;
  }
}
