import { ApplicationConfig, importProvidersFrom, inject } from '@angular/core';
import { provideRouter, Router } from '@angular/router';
import { routes } from './app.routes';
import { HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { catchError } from 'rxjs/operators';
import { EMPTY, Observable, throwError } from 'rxjs';
import { isTokenExpired } from './services/token-utils';
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    importProvidersFrom(HttpClientModule),
    provideAnimations(),
    provideHttpClient(
      withInterceptors([
        (req, next) => {
          const router = inject(Router);
          const token = localStorage.getItem("jwtToken");

          if (req.url.includes('/auth/login') || req.url.includes('/auth/register')) {
            return next(req);
          }

          if (token) {
            if (isTokenExpired(token)) {
              console.warn("Expired token detected, redirecting to login.");
              localStorage.removeItem('jwtToken');
              router.navigate(['/']);
              return EMPTY;   // stop request
            }

            req = req.clone({
              setHeaders: { Authorization: `Bearer ${token}` }
            });
          }

          return next(req).pipe(
            catchError(err => {
              console.error("Interceptor caught error: ", err);
              if (err.status === 401 || err.status === 403) {
                localStorage.removeItem("jwtToken");
                router.navigate(['/']);
              }
              return throwError(() => err);
            })
          );
        }
      ])

    )
  ]
};
