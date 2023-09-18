import { HttpClient } from "@angular/common/http";
import { ApiPath } from "../_model/ApiPath";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root',
})
export class ApiService {

  constructor(private httpClient: HttpClient) {
  }

  public get<T = any>(url: ApiPath, options?: any): Observable<T>{
    return this.httpClient.get<T>(`${ApiPath.apiBaseUrl}` + `${url}`)
  }

  public post<T = any, K = any>(url: ApiPath, body?: K, options?: any): Observable<T> {
    return this.httpClient.post<T>(`${ApiPath.apiBaseUrl}` + `${url}`, body);
  }

  public put<T = any, K = any>(url: ApiPath, body?: K, options?: any): Observable<T> {
    return this.httpClient.put<T>(`${ApiPath.apiBaseUrl}` + `${url}`, body);
  }

  public patch<T = any, K = any>(url: ApiPath, body?: K, options?: any): Observable<T> {
    return this.httpClient.patch<T>(`${ApiPath.apiBaseUrl}` + `${url}`, body);
  }

  public delete<T = any>(url: ApiPath, options?: any): Observable<T> {
    return this.httpClient.delete<T>(`${ApiPath.apiBaseUrl}` + `${url}`);
  }
}
