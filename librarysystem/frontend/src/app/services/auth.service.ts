import { HttpClient, HttpHandler, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { User } from "../models/user.model";

@Injectable({ providedIn: 'root' })
export class AuthService {
    private baseUrl = "http://localhost:8080/auth"

    constructor(private http: HttpClient) { }

    saveUser(user: User): Observable<any> {
        return this.http.post<User>(`${this.baseUrl}/register`, user);
    }

    login(email: String, password: String): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/login`, { email: email, password: password }, { headers: new HttpHeaders({ 'No-Auth': 'True' }) });
    }
}