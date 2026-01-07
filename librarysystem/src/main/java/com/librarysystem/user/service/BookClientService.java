package com.librarysystem.user.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.librarysystem.book.model.Books;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookClientService {
    private static final String BASE_URL = "http://localhost:8080/books";
    private final RestTemplate restTemplate;

    public List<Books> fetchBooksForUser() {
        return Arrays.asList(
            restTemplate.getForObject(BASE_URL+"/getBooks", Books[].class)
        );
    }

    public List<String> fetchAuthors() {
        return Arrays.asList(
            restTemplate.getForObject(BASE_URL+"/authors", String[].class)
        );
    }

    public List<String> fetchGenres() {
        return Arrays.asList(
            restTemplate.getForObject(BASE_URL+"/genres", String[].class)
        );
    }
    
    public List<Books> searchBooks(String title, Optional<List<String>> genre, Optional<List<String>> author) {
        String url = BASE_URL + "/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        builder.queryParam("title", title);
        genre.ifPresent(gList -> gList.forEach(g -> builder.queryParam("genres", g)));
        author.ifPresent(aList -> aList.forEach(a -> builder.queryParam("authors", a)));

        URI uri = builder.build().encode().toUri();
        ResponseEntity<Books[]> response = restTemplate.getForEntity(uri, Books[].class);
        return Arrays.asList(response.getBody());
    }

    public void saveBook(Books book) {
        restTemplate.postForObject(BASE_URL+"/saveBook", book, Books.class);
    }

    public void deleteBook(Long id) {
        restTemplate.delete(BASE_URL + "/delete/" + id);
    }

    public void updateBook(Long id, Map<String, Object> updates) {
        restTemplate.patchForObject(BASE_URL+"/updateBook?id=" + id, updates, void.class);
    }

}
