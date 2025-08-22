package com.librarysystem.book.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.librarysystem.book.model.Books;

@Repository
public interface BookRepository extends JpaRepository<Books, Long>{
    @Query("SELECT DISTINCT b.genre FROM Books b")
    List<String> findAllGenres();
    
    @Query("SELECT DISTINCT b.author FROM Books b")
    List<String> findAllAuthors();
    
    @Query("SELECT b FROM Books b " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:genres IS NULL OR LOWER(b.genre) IN (:genres)) " +
            "AND (:authors IS NULL OR LOWER(b.author)  IN (:authors))"
            )
    List<Books> searchBooks(String title, List<String> genres, List<String> authors);
}
