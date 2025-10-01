import { Component, OnInit } from "@angular/core";
import { BorrowRecord } from "../../models/borrowrecord.model";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { BorrowBookService } from "../../services/borrow.service";
import { Router } from "@angular/router";

@Component({
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './view-myprofile.component.html',
    styleUrls: ['./view-myprofile.component.scss']
})
export class ViewMyProfileComponent implements OnInit {
    successMessage: string | null = null;
    errorMessage: string | null = null;

    borrowRecords: BorrowRecord[] = [];
    constructor(private borrowService: BorrowBookService, private router: Router) { };

    ngOnInit(): void {
        this.loadUserBorrowRecords();
    }

    loadUserBorrowRecords(): any {
        this.borrowService.loadUserBorrowRecords().subscribe(data => this.borrowRecords = data);
    }

    onReturn(borrowRecord: BorrowRecord) {
        this.borrowService.returnBook(borrowRecord.id)
            .subscribe({
                next: (data) => {
                    this.successMessage = "Book returned.";
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
        this.router.navigate(['/view-myprofile']);
    }

    onErrorClose() {
        this.errorMessage = null;
    }
}