package com.librarysystem.book.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.book.dto.BookUpdateRequest;
import com.librarysystem.book.model.Books;
import com.librarysystem.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/saveBook")
    public void saveBook(@RequestBody Books book) {
        bookService.saveBook(book);
    }

    @GetMapping("/getBooks")
    public List<Books> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/authors")
    public List<String> getAllAuthors() {
        return bookService.getAuthors();
    }

    @GetMapping("/genres")
    public List<String> getAllGenres() {
        return bookService.getGenres();
    }

    @GetMapping("/searchBooks")
    public List<Books> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) List<String> genres, 
                                    @RequestParam(required = false) List<String> authors) {
        return bookService.searchBooks(title, genres, authors);
    }

    @PatchMapping("/updateBook")
    public ResponseEntity<String> updateBook(@RequestParam Long id, @RequestBody BookUpdateRequest updates) {
        Optional<Books> optionalbook = bookService.findBookById(id);
        log.info("Update request for book with id: {}", id);
        if(optionalbook.isPresent()) {
            Books book = optionalbook.get();
            if(updates.author() != null) book.setAuthor(updates.author());
            if(updates.genre() != null) book.setGenre(updates.genre());
            if(updates.title() != null) book.setTitle(updates.title());
            if(updates.imageUrl() != null) {
                String trimmedImageUrl = updates.imageUrl().trim();
                if(!trimmedImageUrl.isEmpty()) {
                    book.setImageUrl(trimmedImageUrl);
                }
            }
            if(updates.quantity() != null) book.setQuantity(updates.quantity());
            bookService.saveBook(book);
            return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Optional<Books> optionalBook = bookService.findBookById(id);
        if(optionalBook.isPresent()) {
            bookService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
    }
}