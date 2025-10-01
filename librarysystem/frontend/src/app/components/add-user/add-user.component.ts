import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { User } from "../../models/user.model";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";

@Component({
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './add-user.component.html',
    styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent {
    successMessage: string | null = null;
    errorMessage: string | null = null;

    addUserForm: FormGroup;
    constructor(private authService: AuthService, private fb: FormBuilder, private router: Router) {
        this.addUserForm = this.fb.group({
            firstname: ['', Validators.required],
            lastname: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            mobile: ['', Validators.required],
            password: ['', Validators.required],
            role: ['', Validators.required]
        })
    }

    onSubmit() {
        if (this.addUserForm.valid) {
            const newUser: User = this.addUserForm.value;
            this.authService.saveUser(newUser).subscribe({
                next: (data) => {
                    this.successMessage = "User saved successfully!";
                    this.errorMessage = null;
                },
                error: (err) => {
                    this.errorMessage = err.error.error || "Error saving user.";
                    this.successMessage = null;
                }
            });
        }
    }

    onSuccessOk() {
        this.successMessage = null;
        this.router.navigate(['/']);
    }

    onErrorClose() {
        this.errorMessage = null;
    }
}