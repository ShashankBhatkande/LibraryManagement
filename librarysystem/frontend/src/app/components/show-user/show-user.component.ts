import { Component, OnInit } from "@angular/core";
import { Route, RouterLink } from "@angular/router";
import { User } from "../../models/user.model";
import { FormControl } from "@angular/forms";
import { UserService } from "../../services/user.service";
import { CommonModule } from "@angular/common";
import { RoleService } from "../../services/role.service";

@Component({
    standalone: true,
    imports: [CommonModule],
    templateUrl: './show-user.component.html',
    styleUrl: './show-user.component.scss'
})
export class ShowUserComponent implements OnInit {
    constructor(private userService: UserService, public roleService: RoleService) { }
    users: User[] = [];
    statusControl = new FormControl('');

    successMessage: string | null = null;
    errorMessage: string | null = null;

    status: string = '';

    ngOnInit(): void {
        this.loadAllUsers();
    }

    loadAllUsers() {
        this.userService.getAllUsers().subscribe(data => this.users = data);
    }

    onApprove(user: User) {
        this.userService.approveUser(user.id).subscribe({
            next: (res) => {
                this.successMessage = "User approved.";
                this.errorMessage = null;
            },
            error: (err) => {
                this.errorMessage = err.error.error;
                this.successMessage = null;
            }
        });
    }

    onDelete(user: User) {
        this.userService.deleteUser(user.id).subscribe({
            next: () => {
                this.successMessage = "User deleted.";
                this.errorMessage = null;
            },
            error: (err) => {
                this.errorMessage = err.error.error;
                this.successMessage = null;
            }
        });
    }

    onReject(user: User) {
        this.userService.deleteUser(user.id).subscribe({
            next: () => {
                this.successMessage = "User rejected.";
                this.errorMessage = null;
            },
            error: (err) => {
                this.errorMessage = err.error.error;
                this.successMessage = null;
            }
        });
    }

    onSuccessOk() {
        this.successMessage = null;
        this.loadAllUsers();
    }

    onErrorClose() {
        this.errorMessage = null;
    }
}