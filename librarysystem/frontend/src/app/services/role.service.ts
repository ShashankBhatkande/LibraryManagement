import { Injectable } from "@angular/core";
import { jwtDecode } from "jwt-decode";

export interface JwtPayload {
    exp: number;
    iat?: number;
    sub?: string;
    roles?: string[];
}
@Injectable({ providedIn: 'root' }) 
export class RoleService {
    getRoles(): string[] {
        const token = localStorage.getItem("jwtToken");
        if(!token) return[];
        try {
            const decoded = jwtDecode<JwtPayload>(token);
            return decoded.roles || [];
        } catch {
            return [];
        }
    }

    hasAtLeast(role: 'ROLE_USER' | 'ROLE_LIBRARIAN' | 'ROLE_ADMIN'): boolean {
        const hierarchy = ['ROLE_USER', 'ROLE_LIBRARIAN', 'ROLE_ADMIN'];
        const userRoles = this.getRoles();

        let maxUserLevel = -1;
        userRoles.forEach(r => {
            const idx = hierarchy.indexOf(r);
            if(idx > maxUserLevel) maxUserLevel = idx;
        });

        const requiredLevel = hierarchy.indexOf(role);
        return maxUserLevel >= requiredLevel;
    }
}