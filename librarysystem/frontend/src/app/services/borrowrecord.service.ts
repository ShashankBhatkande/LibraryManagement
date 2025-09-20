import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { EMPTY, Observable } from "rxjs";
import { BorrowRecord } from "../models/borrowrecord.model";
import { jwtDecode } from "jwt-decode";

@Injectable({ providedIn: 'root'})
export class BorrowRecordService {
    constructor(private http: HttpClient){};

    loadUserBorrowRecords(): Observable<BorrowRecord[]> {
        const token = localStorage.getItem('jwtToken');
        if(!token) {
            console.error("No token found");
            return EMPTY
        }
        const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
        return this.http.get<BorrowRecord[]>(`http://localhost:8080/transactions/getRecords`, { headers });
    }
}