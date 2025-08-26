import { Component } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { BookService } from "../../services/book.service";
import { ActivatedRoute, Router } from "@angular/router";
import { Book } from "../../models/book.model";
import { CommonModule } from "@angular/common";

@Component({
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './update-book.component.html',
    styleUrls: ['./update-book.component.css']
})
export class UpdateBookComponent{
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

        console.log("Submit clicked, form value:", this.updateBookForm.value);
        if(this.updateBookForm.valid) {
            const { ...formData } = this.updateBookForm.value;
            const id = Number(this.route.snapshot.paramMap.get('id'));
            const updateBook: any = {};

            Object.keys(formData).forEach(key => {
                if(formData[key] !== null && formData[key] !== '') {
                    updateBook[key] = formData[key];
                }
            });

            this.bookService.updateBook(id, updateBook).subscribe ({
                next: (data) => {
                    console.log("Book updated successfully: ", data);
                    this.router.navigate(['/books']);
                },
                error: (err) => console.log('Error book updating. ', err)
            });
        } else {
            console.warn("Form is invalid:", this.updateBookForm.errors, this.updateBookForm.value);
        }
    }
}