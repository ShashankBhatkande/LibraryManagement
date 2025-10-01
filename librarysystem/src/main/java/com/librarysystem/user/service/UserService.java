package com.librarysystem.user.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.librarysystem.book.model.Books;
import com.librarysystem.user.model.AccountStatus;
import com.librarysystem.user.model.User;
import com.librarysystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final PasswordEncoder encoder;

    public User saveUser(User user) throws Exception {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email Already Exists");
        }

        if (user.getRole().equals("USER")) {
            user.setStatus(AccountStatus.APPROVED);
        } else {
            user.setStatus(AccountStatus.PENDING);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Books> fetchBooksForUser() {
        String url = "http://localhost:8080/books/getBooks";
        ResponseEntity<Books[]> response = restTemplate.getForEntity(url, Books[].class);
        return Arrays.asList(response.getBody());
    }

    public List<String> fetchAuthors() {
        String url = "http://localhost:8080/books/authors";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
        return Arrays.asList(response.getBody());
    }

    public List<String> fetchGenres() {
        String url = "http://localhost:8080/books/genres";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
        return Arrays.asList(response.getBody());
    }

    public void saveBook(Books book) {
        String url = "http://localhost:8080/books/saveBook";
        restTemplate.postForObject(url, book, Books.class);
    }

    public List<Books> searchBooks(String title, Optional<List<String>> genre, Optional<List<String>> author) {
        String url = "http://localhost:8080/books/searchBooks";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        builder.queryParam("title", title);
        genre.ifPresent(gList -> gList.forEach(g -> builder.queryParam("genres", g)));
        author.ifPresent(aList -> aList.forEach(a -> builder.queryParam("authors", a)));

        URI uri = builder.build().encode().toUri();
        ResponseEntity<Books[]> response = restTemplate.getForEntity(uri, Books[].class);
        return Arrays.asList(response.getBody());
    }

    public void deleteBook(Long id) {
        String url = "http://localhost:8080/books/delete/" + id;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class);
    }

    public void updateBook(Long id, Map<String, Object> updates) {
        String url = "http://localhost:8080/books/updateBook?id=" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(updates, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                requestEntity,
                String.class);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepository.deleteById(id);
    }

    public void rejectUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setStatus(AccountStatus.REJECTED);
        userRepository.save(user);

    }

    public void approveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setStatus(AccountStatus.APPROVED);
        userRepository.save(user);
    }
}