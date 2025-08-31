import { HttpClient, HttpHeaders } from "@angular/common/http";
import { ApiPath } from "../_model/ApiPath";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";

@Injectable({ providedIn: 'root' })
export class ApiService {

	private readonly baseUrl = (environment.apiUrl ?? '').replace(/\/+$/, '');

	constructor(private httpClient: HttpClient) {}

	public get<T = any>(url: ApiPath, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('GET', this.joinUrl(url), { params, headers });
	}

	public getFormatted<T = any>(url: ApiPath, paramArray: string[], params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('GET', this.formatWithBaseUrl(`${url}`, paramArray), { params, headers });
	}

	public post<T = any, K = any>(url: ApiPath, body?: K, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('POST', this.joinUrl(url), { body, params, headers });
	}

	public postFormatted<T = any, K = any>(url: ApiPath, paramArray: string[], body?: K, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('POST', this.formatWithBaseUrl(`${url}`, paramArray), { body, params, headers });
	}

	public put<T = any, K = any>(url: ApiPath, body?: K, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('PUT', this.joinUrl(url), { body, params, headers });
	}

	public putFormatted<T = any, K = any>(url: ApiPath, paramArray: string[], body?: K, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('PUT', this.formatWithBaseUrl(`${url}`, paramArray), { body, params, headers });
	}

	public patch<T = any, K = any>(url: ApiPath, body?: K, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('PATCH', this.joinUrl(url), { body, params, headers });
	}

	public patchFormatted<T = any, K = any>(url: ApiPath, paramArray: string[], body?: K, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('PATCH', this.formatWithBaseUrl(`${url}`, paramArray), { body, params, headers });
	}

	public delete<T = any>(url: ApiPath, params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('DELETE', this.joinUrl(url), { params, headers });
	}

	public deleteFormatted<T = any>(url: ApiPath, paramArray: string[], params?: any, headers?: HttpHeaders | { [header: string]: string | string[] }): Observable<T> {
		return this.request<T>('DELETE', this.formatWithBaseUrl(`${url}`, paramArray), { params, headers });
	}

	private request<T>(
		method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE',
		url: string,
		opts: {
			params?: any;
			body?: any;
			headers?: HttpHeaders | { [header: string]: string | string[] };
		} = {}
	): Observable<T> {
		const reqOptions: {
			body?: any;
			params?: any;
			headers?: HttpHeaders | { [header: string]: string | string[] };
			withCredentials: boolean;
			observe: 'body';
			responseType: 'json';
		} = {
			body: opts.body,
			params: opts.params,
			headers: opts.headers,
			withCredentials: true,
			observe: 'body',
			responseType: 'json',
		};

		return this.httpClient.request<T>(method, url, reqOptions);
	}

	private format(str: string, paramArray: string[]): string {
		if (!paramArray) return str;
		return str.replace(/{(\d+)}/g, (match, index) => {
			return typeof paramArray[index] !== 'undefined' ? paramArray[index] : match;
		});
	}

	private formatWithBaseUrl(str: string, paramArray: string[]): string {
		return this.joinUrl(this.format(`${str}`, paramArray));
	}

	private joinUrl(path: string): string {
		const p = `${path ?? ''}`;
		const normalized = p.startsWith('/') ? p : `/${p}`;
		return `${this.baseUrl}${normalized}`.replace(/(?<!:)\/{2,}/g, '/'); // nie rusza "http://"
	}
}
