import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { jwtDecode } from "jwt-decode";
import { EMPTY } from "rxjs";
@Injectable({ providedIn: 'root' })
export class BorrowBookService {
    constructor(private http: HttpClient){}

    borrowBook(bookId: number): any {
        const token = localStorage.getItem('jwtToken');
        if(!token) {
            console.error("No token found");
            return EMPTY;
        }
        try {
            const decoded: any = jwtDecode(token);
            const headers = new HttpHeaders().set("Authorization",`Bearer ${token}`);

            const body = {
                username: decoded.sub,
                bookId: bookId
            };
            
            return this.http.post(`http://localhost:8080/transactions/borrow`, body, { headers });
        } catch(error) {
            console.error("Invalid token ", error);
            return EMPTY    ;
        }
    }
}
