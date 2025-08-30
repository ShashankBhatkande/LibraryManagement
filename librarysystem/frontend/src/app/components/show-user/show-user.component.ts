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
export class ShowUserComponent implements OnInit{
    constructor(private userService: UserService, public roleService: RoleService){}
    users: User[] = [];
    statusControl = new FormControl('');

    status: string = '';

    ngOnInit(): void {
        this.loadAllUsers();
    }

    loadAllUsers() {
        this.userService.getAllUsers().subscribe(data => this.users = data);
    }

    onApprove(user: User) {
        console.log("Approving User");
        this.userService.approveUser(user.id).subscribe ({
            next: ()=> {
                alert("User Approved");
                this.loadAllUsers();
            }, 
            error:() => {
                alert(`Error approving user.`);
            }
        });
    }

    onDelete(user: User) {
        console.log("Approving User");
        this.userService.rejectUser(user.id).subscribe ({
            next: ()=> {
                alert("User Rejected");
                this.loadAllUsers();
            }, 
            error:() => {
                alert(`Error approving user.`);
            }
        });
    }
}