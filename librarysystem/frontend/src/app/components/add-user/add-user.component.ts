import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { UserService } from "../../services/user.service";
import { P } from "@angular/cdk/keycodes";
import { User } from "../../models/user.model";
import { Router } from "@angular/router";

@Component({
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './add-user.component.html',
    styleUrls: ['./add-user.component.css']
})
export class addUserComponent {
    addUserForm: FormGroup;
    constructor(private userService: UserService, private fb: FormBuilder, private router: Router) {
        this.addUserForm = this.fb.group({
            firstname: ['', Validators.required],
            lastname: ['', Validators.required],
            email:['', Validators.required],
            mobile:['', Validators.required],
            password:['', Validators.required],
            role:['', Validators.required]
        })
    }

    onSubmit() {
        if(this.addUserForm.valid) {
            const newUser: User = this.addUserForm.value;
            this.userService.saveUser(newUser).subscribe({
                next: (data) => {
                    console.log("User saved successfully: ", data);
                    this.router.navigate(['/']);
                },
                error: (err) => console.error('Error saving user: ', err)
            });
        }
    }
}