import { Component } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { BookService } from "../../services/book.service";
import { Book } from "../../models/book.model";
import { Router } from "@angular/router";

@Component({
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './add-book.component.html',
    styleUrls : ['./add-book.component.css']
})
export class AddBookComponent {
    addBookForm: FormGroup;
    constructor(private fb: FormBuilder, private bookService: BookService, private router: Router) {
        this.addBookForm = this.fb.group ({
            title: ['', Validators.required],
            genre: ['', Validators.required],
            author: ['', Validators.required],
            quantity: [0, [Validators.required, Validators.min(1)]],
            imageUrl: ['']
        })
    }

    onSubmit() {
        if(this.addBookForm.valid) {
            const newBook: Book = this.addBookForm.value;
            this.bookService.saveBook(newBook).subscribe ({
                next: (data) => {
                    console.log("Book saved successfully: ", data);
                    this.router.navigate(['/books']);
                },
                error: (err) =>  console.error('Error saving book: ', err)
            });
        }
    }
}