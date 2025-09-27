package com.librarysystem.book.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.librarysystem.book.model.Books;
import com.librarysystem.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public void saveBook(Books book) {
        bookRepository.save(book);
    }

    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Books> searchBooks(String title, List<String> genres, List<String> authors) {
        return bookRepository.searchBooks(title, genres, authors);
    }

    public List<String> getAuthors() {
        return bookRepository.findAllAuthors();
    }

    public List<String> getGenres() {
        return bookRepository.findAllGenres();
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Books> findBookById(Long id) {
        return bookRepository.findById(id);
    }
}
