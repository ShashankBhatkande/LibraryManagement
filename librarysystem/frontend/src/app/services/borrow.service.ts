import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { jwtDecode } from "jwt-decode";
import { EMPTY, Observable } from "rxjs";
import { BorrowRecord } from "../models/borrowrecord.model";

@Injectable({ providedIn: 'root' })
export class BorrowBookService {
    constructor(private http: HttpClient) { }

    borrowBook(bookId: number): Observable<any> {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            console.error("No token found");
            return EMPTY;
        }
        try {
            const decoded: any = jwtDecode(token);
            const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);

            const body = {
                username: decoded.sub,
                bookId: bookId
            };

            return this.http.patch(`http://localhost:8080/transactions/borrow`, body, { headers });
        } catch (error) {
            console.error("Invalid token ", error);
            return EMPTY;
        }
    }

    loadUserBorrowRecords(): Observable<any> {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            console.error("No token found");
            return EMPTY
        }
        const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
        return this.http.get<BorrowRecord[]>(`http://localhost:8080/transactions/user-records`, { headers });
    }

    loadBorrowRecords(): Observable<any> {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            console.error("No token found");
            return EMPTY;
        }
        const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
        return this.http.get<BorrowRecord[]>(`http://localhost:8080/transactions`, { headers });
    }
    returnBook(id: number): Observable<any> {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            console.error("No token found");
            return EMPTY;
        }
        try {
            const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
            const body = {
                id: id
            };

            return this.http.patch('http://localhost:8080/transactions/return-book', body, { headers });
        } catch (error) {
            console.error("Invalid token: ", error);
            return EMPTY;
        }
    }

    confirmReturn(id: number): Observable<any> {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            console.error("No Token found");
            return EMPTY;
        }
        try {
            const decoded = jwtDecode(token);
            const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
            const body = {
                id: id
            };

            return this.http.patch('http://localhost:8080/transactions/confirm-return', body, { headers });
        } catch (error) {
            console.error("Invalid token: ", error);
            return EMPTY;
        }
    }
}
