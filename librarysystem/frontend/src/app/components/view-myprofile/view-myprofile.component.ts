import { Component, OnInit } from "@angular/core";
import { BorrowRecord } from "../../models/borrowrecord.model";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { BorrowBookService } from "../../services/borrow.service";

@Component({
    standalone: true,
    imports:[CommonModule, ReactiveFormsModule],
    templateUrl: './view-myprofile.component.html',
    styleUrls: ['./view-myprofile.component.scss']
})
export class ViewMyProfileComponent implements OnInit{
    borrowRecords: BorrowRecord[] = [];
    constructor(private borrowService: BorrowBookService ){};

    ngOnInit(): void {
        this.loadUserBorrowRecords();
    }

    loadUserBorrowRecords(): any {
        this.borrowService.loadUserBorrowRecords().subscribe(data => this.borrowRecords = data);
    }

    onReturn(borrowRecord: BorrowRecord) {
        this.borrowService.returnBook(borrowRecord.id)
        .subscribe(() => {
            this.loadUserBorrowRecords();
        });
    }
}