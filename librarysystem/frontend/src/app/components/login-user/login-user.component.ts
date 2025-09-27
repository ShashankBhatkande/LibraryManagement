import { Component } from "@angular/core";
import { FormBuilder, FormsModule } from "@angular/forms";
import { Router, RouterModule } from "@angular/router";
import { AuthService } from "../../services/auth.service";

@Component({
    standalone: true,
    imports: [ FormsModule, RouterModule ],
    templateUrl: './login-user.component.html',
    styleUrls: ['/login-user.component.scss']
})
export class LoginUserComponent {
    email = '';
    password = '';

    constructor(private authService: AuthService, private router: Router){}

    onLogin(): void {
        const credentials = { email: this.email, password: this.password}

        this.authService.login(this.email, this.password).subscribe ({
            next: (res) => {
                if(res && res.token) {
                    // Clear previous login data
                    localStorage.removeItem("jwtToken");
                    localStorage.removeItem("userEmail");
                    localStorage.removeItem("name");

                    // Store new login data
                    localStorage.setItem("jwtToken", res.token);
                    localStorage.setItem("userEmail", this.email);  // save current email
                    localStorage.setItem("name", res.username);
                    this.router.navigate(['/books']);
                }
            },
            error: (err) => {
                console.error('Error login: ', err);
                alert(err.error.error);
            }
        });
    }
}