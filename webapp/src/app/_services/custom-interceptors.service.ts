import { Injectable } from "@angular/core";
import { TokenStorageService } from "./token-storage.service";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable()
export class CustomInterceptorsService implements HttpInterceptor{

  constructor(
    readonly tokenStorageService: TokenStorageService
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenStorageService.getToken();
    if (!!token)
      req = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + token)});
    return next.handle(req);
  }

}
