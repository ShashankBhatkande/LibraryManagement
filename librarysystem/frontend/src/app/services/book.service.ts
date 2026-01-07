import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';

@Injectable({ providedIn: 'root' })
export class BookService {
  private baseUrl = 'http://localhost:8080/books';

  constructor(private http: HttpClient) { }

  getAllBooks(): Observable<any> {
    return this.http.get<Book[]>(`${this.baseUrl}/get-books`);
  }

  getGenres(): Observable<any> {
    return this.http.get<string[]>(`${this.baseUrl}/genres`);
  }

  getAuthors(): Observable<any> {
    return this.http.get<string[]>(`${this.baseUrl}/authors`);
  }

  filterBooks(title: string, genres: string[], authors: string[]): Observable<any> {
    let params = new HttpParams();
    if (title) params = params.set('title', title);
    genres.forEach(g => params = params.append('genres', g));
    authors.forEach(a => params = params.append('authors', a));

    return this.http.get<Book[]>(`${this.baseUrl}/search`, { params });
  }

  saveBook(book: Book): Observable<any> {
    return this.http.post<Book>(`${this.baseUrl}/save-book`, book);
  }

  updateBook(id: number, book: Book): Observable<any> {
    let params = new HttpParams().set("id", id);
    return this.http.patch(`${this.baseUrl}/update-book`, book, { params });
  }

  deleteBook(id: number): Observable<any> {
    let params = new HttpParams().set("id", id);
    return this.http.delete(`${this.baseUrl}/delete-book`, { params });
  }
}
