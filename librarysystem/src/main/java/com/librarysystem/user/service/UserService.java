package com.librarysystem.user.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.librarysystem.book.model.Books;
import com.librarysystem.user.model.User;
import com.librarysystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<Books> fetchBooksForUser() {
        String url = "http://localhost:8080/books/getBooks";
        ResponseEntity<Books[]> response =  restTemplate.getForEntity(url, Books[].class);
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

    public List<Books> searchBooks(Optional<?> title, Optional<List<String>> genre, Optional<List<String>> author) {
        String url = "http://localhost:8080/books/searchBooks";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        title.ifPresent(t -> builder.queryParam("title", t));
        genre.ifPresent(gList -> gList.forEach(g -> builder.queryParam("genres", g)));
        author.ifPresent(aList -> aList.forEach(a -> builder.queryParam("authors", a)));

        URI uri = builder.build().encode().toUri();
        ResponseEntity<Books[]> response = restTemplate.getForEntity(uri, Books[].class);
        return Arrays.asList(response.getBody());
    }

    public ResponseEntity<String> deleteBook(Long id) {
        String url = "http://localhost:8080/books/delete/" + id;
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class
            );
            return response;
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error" + e.getMessage());
        }
    }

    public ResponseEntity<String> updateBook(Long id, Map<String, Object> updates) {
        String url = "http://localhost:8080/books/updateBook?id=" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(updates, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                requestEntity,
                String.class
            );
            return response;
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error" + e.getMessage());
        }
    }
}
