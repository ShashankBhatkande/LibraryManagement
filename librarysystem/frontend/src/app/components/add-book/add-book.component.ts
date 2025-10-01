import { Component } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { BookService } from "../../services/book.service";
import { Book } from "../../models/book.model";
import { Router } from "@angular/router";
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './add-book.component.html',
    styleUrls: ['./add-book.component.scss']
})
export class AddBookComponent {
    addBookForm: FormGroup;

    successMessage: string | null = null;
    errorMessage: string | null = null;

    constructor(private fb: FormBuilder, private bookService: BookService, private router: Router, private snackBar: MatSnackBar) {
        this.addBookForm = this.fb.group({
            title: ['', Validators.required],
            genre: ['', Validators.required],
            author: ['', Validators.required],
            quantity: [0, [Validators.required, Validators.min(1)]],
            imageUrl: ['']
        })
    }

    onSubmit() {
        if (this.addBookForm.valid) {
            const newBook: Book = this.addBookForm.value;
            this.bookService.saveBook(newBook).subscribe({
                next: () => {
                    this.snackBar.open("Book Saved successfully.", "Ok", { duration: 3000});
                    this.router.navigate(['/books']);
                    this.errorMessage = null;
                },
                error: (err) => {
                    this.errorMessage = err.error.error || "Error saving book.";
                    this.successMessage = null;
                }
            });
        }
    }


    OnErrorOk() {
        this.errorMessage = null;
        this.router.navigate(['/books']);
    }
}