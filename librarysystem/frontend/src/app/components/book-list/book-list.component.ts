import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book.model';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { merge } from 'rxjs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { RoleService } from '../../services/role.service';
import { BorrowBookService } from '../../services/borrow.service';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSidenavModule,
    MatDividerModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule, MatSelectModule, RouterLink],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  genres: string[] = [];
  authors: string[] = [];

  successMessage: string | null = null;
  errorMessage: string | null = null;
  confirmMessage: string | null = null;
  
  bookToDelete: Book | null = null;
  
  viewMode: 'grid' | 'list' = 'grid';
  username: string = '';

  searchControl = new FormControl('');
  genreControl = new FormControl<string[]>([]);
  authorControl = new FormControl<string[]>([]);

  constructor(private bookService: BookService, public roleService: RoleService, public borrowBookService: BorrowBookService, private router: Router) { }

  ngOnInit(): void {
    const email = localStorage.getItem('userEmail');
    const username = localStorage.getItem('name') ?? 'Guest';
    this.username = username;
    this.loadAllBooks();
    this.loadAllAuthors();
    this.loadAllGenres();
    // Load dropdown options
    this.bookService.getGenres().subscribe(data => this.genres = data);
    this.bookService.getAuthors().subscribe(data => this.authors = data);

    // Trigger filter when any control changes
    merge(
      this.searchControl.valueChanges.pipe(debounceTime(300), distinctUntilChanged()),
      this.genreControl.valueChanges,
      this.authorControl.valueChanges
    ).subscribe(() => this.applyFilters());
  }

  loadAllBooks() {
    this.bookService.getAllBooks().subscribe(data => this.books = data);
  }
  loadAllAuthors() {
    this.bookService.getAuthors().subscribe(data => this.authors = data);
  }
  loadAllGenres() {
    this.bookService.getGenres().subscribe(data => this.genres = data);
  }
  setViewMode(mode: 'grid' | 'list') {
    this.viewMode = mode;
  }

  applyFilters() {
    const title = this.searchControl.value ?? '';
    const genres = this.genreControl.value ?? [];
    const authors = this.authorControl.value ?? [];

    this.bookService.filterBooks(title, genres, authors)
      .subscribe(data => this.books = data);
  }

  onDelete(book: Book) {
    this.confirmMessage = `Are you sure you want to delete "${book.title}"?`;
    this.bookToDelete = book;
  }

  onBorrow(book: Book) {
    this.borrowBookService.borrowBook(book.id).subscribe({
      next: (res: any) => {
        this.successMessage = "Book Borrowed Successfully.";
        this.errorMessage = null;
      },
      error: (err: any) => {
        this.errorMessage = err.error.error || "Failed to borrow.";
        this.successMessage = null;
      }
    })
  }

  onSuccessOk() {
    this.successMessage = null;
    this.loadAllBooks();
  }

  onErrorClose() {
    this.errorMessage = null;
    this.loadAllBooks();
  }

  onConfirmYes() {
    if (this.bookToDelete) {
      this.bookService.deleteBook(this.bookToDelete.id).subscribe({
        next: () => {
          this.successMessage = "Book Deleted Successfully.";
          this.errorMessage = null;
          this.loadAllBooks();
          this.loadAllAuthors();
          this.loadAllGenres();
        },
        error: (err) => {
          this.errorMessage = err.error.error || "Error deleting book.";
          this.successMessage = null;
        }
      });
    }
    this.confirmMessage = null;
    this.bookToDelete = null;
  }

  onConfirmNo() {
    this.confirmMessage = null;
    this.bookToDelete = null;
  }

}
