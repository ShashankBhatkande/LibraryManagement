import { Component, OnInit } from "@angular/core";
import { BorrowRecord } from "../../models/borrowrecord.model";
import { BorrowRecordService } from "../../services/borrowrecord.service";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";

@Component({
    standalone: true,
    imports:[CommonModule, ReactiveFormsModule],
    templateUrl: './view-profile.component.html',
    styleUrl: './view-profile.component.scss'
})
export class ViewProfileComponent implements OnInit{
    borrowRecords: BorrowRecord[] = [];
    constructor(private borrowRecordService: BorrowRecordService){};

    ngOnInit(): void {
        this.loadUserBorrowRecords();
    }

    loadUserBorrowRecords(): any {
        this.borrowRecordService.loadUserBorrowRecords().subscribe(data => this.borrowRecords = data);
    }
}