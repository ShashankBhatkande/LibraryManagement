package com.librarysystem.borrow.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import com.librarysystem.book.model.Books;
import com.librarysystem.book.repository.BookRepository;
import com.librarysystem.borrow.model.BorrowStatus;
import com.librarysystem.borrow.repository.BorrowRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowPolicyService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final FineService fineService;

    public void validateBorrow(Long userId, Long bookId) {
        Books book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalStateException("Book not found."));

        if (book.getQuantity() <= 0) {
            throw new IllegalStateException("Book is out of stock.");
        }
        
        BigDecimal totalFine = fineService.getTotalFine(userId);
        if (totalFine.compareTo(new BigDecimal("500")) > 0) {
            throw new IllegalStateException("Please pay existing fine before borrowing.");
        }
        
        int borrowedCount = borrowRecordRepository.countByUserIdAndBorrowStatusIn(userId,
                List.of(BorrowStatus.BORROWED, BorrowStatus.OVERDUE, BorrowStatus.RETURNED));
        if(borrowedCount >= 5) {
            throw new IllegalStateException("Borrowing limit reached. Return some books to borrow more.");
        }   

    }
}
