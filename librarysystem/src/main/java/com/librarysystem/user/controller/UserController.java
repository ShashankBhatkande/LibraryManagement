package com.librarysystem.user.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.librarysystem.book.model.Books;
import com.librarysystem.user.model.User;
import com.librarysystem.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User saved successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Error Saving user"));
        }
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.fetchAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @GetMapping("/getBooks")
    public ResponseEntity<?> getAllBooks() {
        try {
            List<Books> books = userService.fetchBooksForUser();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<?> getAllGenres() {
        try {
            List<String> genres = userService.fetchGenres();
            return ResponseEntity.ok(genres);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @GetMapping("/authors")
    public ResponseEntity<?> getAllAuthors() {
        try {
            List<String> authors = userService.fetchAuthors();
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @PostMapping("/saveBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Map<String, String>> saveBook(@RequestBody Books book) {
        try {
            userService.saveBook(book);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book saved successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam(required = false) String title,
            @RequestParam(required = false) Optional<List<String>> genres,
            @RequestParam(required = false) Optional<List<String>> authors) {
        try {
            List<Books> books = userService.searchBooks(title, genres, authors);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @PatchMapping("/updateBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Map<String, String>> updateBook(@RequestParam Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            userService.updateBook(id, updates);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book updated successfully."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Book not Found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @DeleteMapping("/deleteBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Map<String, String>> deleteBook(@RequestParam Long id) {
        try {
            userService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book Deleted Successfully"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Book not Found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Bad Request"));
        }
    }
}
