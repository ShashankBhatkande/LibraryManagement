package com.librarysystem.user.controller;

import java.util.List;
import java.util.Map;
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
    @PreAuthorize("hasRole('ADMIN')")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/getBooks")
    public List<Books> getAllBooks() {
        return userService.fetchBooksForUser();
    }

    @GetMapping("/genres")
    public List<String> getAllGenres() {
        return userService.fetchGenres();
    }

    @GetMapping("/authors")
    public List<String> getAllAuthors() {
        return userService.fetchAuthors();
    }
    @PostMapping("/saveBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public void saveBook(@RequestBody Books book) {
        userService.saveBook(book);
    }
    
    @GetMapping("/search")
    public List<Books> searchBooks(@RequestParam(required = false) Optional<?> title, @RequestParam(required = false) Optional<List<String>> genres, @RequestParam(required = false) Optional<List<String>> authors) {
        return userService.searchBooks(title, genres, authors);
    }

    @PatchMapping("/updateBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Map<String, String>> updateBook(@RequestParam Long id, @RequestBody Map<String, Object> updates) {
        log.info("Update book log inside User Controller with id {}", id);
        ResponseEntity<String> response = userService.updateBook(id, updates);
        if(response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book updated successfully."));
        } else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Book not Found"));
        }
    }

    @DeleteMapping("/deleteBook")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Map<String, String>> deleteBook(@RequestParam Long id) {
        ResponseEntity<String> response = userService.deleteBook(id);
        if(response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book Deleted Successfully"));
        } else if(response.getStatusCode().is4xxClientError()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Book not Found"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Bad Request"));
        }
    }
}
