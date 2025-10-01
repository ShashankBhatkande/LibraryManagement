package com.librarysystem.book.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.librarysystem.book.dto.BookUpdateRequest;
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
        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            book.setTitle(cleanString(book.getTitle(), "Title"));
        } else {
            throw new IllegalArgumentException("Title cannot be empty.");
        }

        if (book.getGenre() != null && !book.getGenre().trim().isEmpty()) {
            book.setGenre(cleanString(book.getGenre(), "Genre"));
        } else {
            throw new IllegalArgumentException("Genre cannot be empty.");
        }

        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            book.setAuthor(cleanString(book.getAuthor(), "Author"));
        } else {
            throw new IllegalArgumentException("Author cannot be empty.");
        }

        if (book.getImageUrl() != null && !book.getImageUrl().trim().isEmpty()) {
            String trimmedImageUrl = book.getImageUrl().trim();
            if (!trimmedImageUrl.isEmpty()) {
                book.setImageUrl(trimmedImageUrl);
            }
        }

        if (book.getQuantity() == null) {
            book.setQuantity(0);
        } else if (book.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
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
        Books book = findBookById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found."));

        bookRepository.deleteById(id);
    }

    public Optional<Books> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    private String cleanString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim().replaceAll("\\s{2,}", " ");
    }

    public void updateBook(Long id, BookUpdateRequest updates) {
        Books book = findBookById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found."));

        if (updates.title() != null && !updates.title().trim().isEmpty()) {
            book.setTitle(cleanString(updates.title(), "Title"));

        }
        if (updates.genre() != null && !updates.genre().trim().isEmpty()) {
            book.setGenre(cleanString(updates.genre(), "Genre"));
        }
        if (updates.author() != null && !updates.author().trim().isEmpty()) {
            book.setAuthor(cleanString(updates.author(), "Author"));
        }
        if (updates.imageUrl() != null && !updates.imageUrl().trim().isEmpty()) {
            String trimmedImageUrl = updates.imageUrl().trim();
            if (!trimmedImageUrl.isEmpty()) {
                book.setImageUrl(trimmedImageUrl);
            }
        }
        if (updates.quantity() != null) {
            if (updates.quantity() < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative.");
            }
            book.setQuantity(updates.quantity());
        }
        bookRepository.save(book);
    }
}
