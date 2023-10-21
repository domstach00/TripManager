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

  public getFormatted<T = any>(url: ApiPath, paramArray: string[], options?: any): Observable<T> {
    return this.httpClient.get<T>(this.formatWithBaseUrl(`${url}`, paramArray))
  }

  public post<T = any, K = any>(url: ApiPath, body?: K, options?: any): Observable<T> {
    return this.httpClient.post<T>(`${ApiPath.apiBaseUrl}` + `${url}`, body);
  }

  public postFormatted<T = any, K = any>(url: ApiPath, paramArray: string[], body?: K, options?: any): Observable<T> {
    return this.httpClient.post<T>(this.formatWithBaseUrl(`${url}`, paramArray), body);
  }

  public put<T = any, K = any>(url: ApiPath, body?: K, options?: any): Observable<T> {
    return this.httpClient.put<T>(`${ApiPath.apiBaseUrl}` + `${url}`, body);
  }

  public putFormatted<T = any, K = any>(url: ApiPath, paramArray: string[], body?: K, options?: any): Observable<T> {
    return this.httpClient.put<T>(this.formatWithBaseUrl(`${url}`, paramArray), body);
  }

  public patch<T = any, K = any>(url: ApiPath, body?: K, options?: any): Observable<T> {
    return this.httpClient.patch<T>(`${ApiPath.apiBaseUrl}` + `${url}`, body);
  }

  public patchFormatted<T = any, K = any>(url: ApiPath, paramArray: string[], body?: K, options?: any): Observable<T> {
    return this.httpClient.patch<T>(this.formatWithBaseUrl(`${url}`, paramArray), body);
  }

  public delete<T = any>(url: ApiPath, options?: any): Observable<T> {
    return this.httpClient.delete<T>(`${ApiPath.apiBaseUrl}` + `${url}`);
  }

  public deleteFormatted<T = any>(url: ApiPath, paramArray: string[], options?: any): Observable<T> {
    return this.httpClient.delete<T>(this.formatWithBaseUrl(`${url}`, paramArray));
  }

  private format(str: string, paramArray: string[]): string {
    if (paramArray) {
      return str.replace(/{(\d+)}/g, (match, index) => {
        return typeof paramArray[index] !== 'undefined'
          ? paramArray[index]
          : match;
      });
    } else {
      return str;
    }
  }

  private formatWithBaseUrl(str: string, paramArray: string[]): string {
    return `${ApiPath.apiBaseUrl}` + this.format(`${str}`, paramArray);
  }
}
