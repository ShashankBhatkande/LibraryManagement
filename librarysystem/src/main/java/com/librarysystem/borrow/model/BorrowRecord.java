package com.librarysystem.borrow.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.librarysystem.book.model.Books;
import com.librarysystem.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book;

    @Enumerated
    private BorrowStatus borrowStatus = BorrowStatus.RETURNED;
    
    private boolean finePaid = false;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private BigDecimal fine;
}
