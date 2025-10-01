import { Component } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { BookService } from "../../services/book.service";
import { ActivatedRoute, Router } from "@angular/router";
import { CommonModule } from "@angular/common";

@Component({
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './update-book.component.html',
    styleUrls: ['./update-book.component.scss']
})
export class UpdateBookComponent {
    successMessage: string | null = null;
    errorMessage: string | null = null;

    updateBookForm: FormGroup;
    constructor(private fb: FormBuilder, private bookService: BookService, private router: Router, private route: ActivatedRoute) {
        this.updateBookForm = this.fb.group({
            title: [''],
            genre: [''],
            author: [''],
            quantity: [null],
            imageUrl: ['']
        })

    }
    onSubmit() {

        if (this.updateBookForm.valid) {
            const { ...formData } = this.updateBookForm.value;
            const id = Number(this.route.snapshot.paramMap.get('id'));
            const updateBook: any = {};

            Object.keys(formData).forEach(key => {
                if (formData[key] !== null && formData[key] !== '') {
                    updateBook[key] = formData[key];
                }
            });

            this.bookService.updateBook(id, updateBook).subscribe({
                next: (data) => {
                    this.successMessage = "Book updated successfully.";
                    this.errorMessage = null;
                },
                error: (err) => {
                    this.errorMessage = err.error.error;
                    this.successMessage = null;
                }
            });
        } else {
            console.warn("Form is invalid:", this.updateBookForm.errors, this.updateBookForm.value);
        }
    }
    onSuccessOk() {
        this.successMessage = null;
        this.router.navigate(['/books']);
    }

    onErrorClose() {
        this.errorMessage = null;
    }
}