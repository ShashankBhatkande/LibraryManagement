import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { User } from "../models/user.model";

@Injectable({ providedIn: 'root'})
export class UserService {
    private baseUrl = 'http://localhost:8080/user';

    constructor(private http: HttpClient){};

    saveUser(user: User): Observable<User> {
        return this.http.post<User>(`${this.baseUrl}/saveUser`, user);
    }
}