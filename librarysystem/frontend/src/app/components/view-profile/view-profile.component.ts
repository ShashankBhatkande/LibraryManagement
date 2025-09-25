import { Component, OnInit } from "@angular/core";
import { BorrowRecord } from "../../models/borrowrecord.model";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { BorrowBookService } from "../../services/borrow.service";
import { RoleService } from "../../services/role.service";

@Component({
    standalone: true,
    imports:[CommonModule, ReactiveFormsModule],
    templateUrl: './view-profile.component.html',
    styleUrl: './view-profile.component.scss'
})
export class ViewProfileComponent implements OnInit{
    borrowRecords: BorrowRecord[] = [];
    constructor(private borrowBookService: BorrowBookService, public roleService: RoleService){};

    ngOnInit(): void {
        this.loadBorrowRecords();
    }

    loadBorrowRecords(): any {
        this.borrowBookService.loadBorrowRecords().subscribe(data => this.borrowRecords = data);
    }

   onConfirm(borrwoRecord: BorrowRecord): any {
        console.log(borrwoRecord.id);
        this.borrowBookService.confirmReturn(borrwoRecord.id) 
        .subscribe(() => {
            this.loadBorrowRecords();
        });
   }
}