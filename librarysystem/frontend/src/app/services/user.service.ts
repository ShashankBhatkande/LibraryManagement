import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { User } from "../models/user.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    getAllUsers(): Observable<any> {
        return this.http.get<User[]>(`http://localhost:8080/user/getUsers`);
    }

    approveUser(id: number): Observable<any> {
        let params = new HttpParams().set("id", id);
        return this.http.patch(`http://localhost:8080/admin/approve`, {}, { params });
    }

    deleteUser(id: number): Observable<any> {
        let params = new HttpParams().set("id", id);
        return this.http.patch(`http://localhost:8080/admin/delete`, {}, { params });
    }

    rejectUser(id: number): Observable<any> {
        let params = new HttpParams().set("id", id);
        return this.http.delete(`http://localhost:8080/admin/reject`, { params });
    }
}