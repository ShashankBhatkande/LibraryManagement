import { jwtDecode } from 'jwt-decode';
export interface JwtPayload {
    exp: number;
    iat?: number;
    sub?: string;
    roles?: string[];
}

export function isTokenExpired(token: string | null): boolean {
    if(!token) return true;
    try {
        const decoded = jwtDecode<JwtPayload>(token);
        if(!decoded.exp) return true;
        const now = Math.floor(Date.now()/1000);
        return decoded.exp < now;
    } catch(e) {
        return true;
    }
}