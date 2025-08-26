import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token =  localStorage.getItem('token');

        if (req.url.includes('/auth/login') || req.url.includes('/auth/register')) {
            return next.handle(req);
        }

        if(token) {
            const cloned = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
            return next.handle(cloned);
        }
        return next.handle(req);
    }
}