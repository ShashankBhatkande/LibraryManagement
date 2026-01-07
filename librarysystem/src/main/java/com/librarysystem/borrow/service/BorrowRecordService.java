package com.librarysystem.borrow.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.librarysystem.book.model.Books;
import com.librarysystem.book.repository.BookRepository;
import com.librarysystem.borrow.dto.BorrowRequest;
import com.librarysystem.borrow.dto.ResponseRecord;
import com.librarysystem.borrow.dto.ReturnBookRequest;
import com.librarysystem.borrow.model.BorrowRecord;
import com.librarysystem.borrow.model.BorrowStatus;
import com.librarysystem.borrow.repository.BorrowRecordRepository;
import com.librarysystem.user.model.User;
import com.librarysystem.user.service.UserService;

import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final FineService fineService;
    private final UserService userService;
    private final BorrowPolicyService borrowPolicyService;
    private final BorrowRecordMapper mapper;

    @Transactional
    public void borrowBook(BorrowRequest request) {
        User user = userService.getUserByEmail(request.username());
        Long userId = user.getId();
        Long bookId = request.bookId();

        borrowPolicyService.validateBorrow(userId, bookId);

        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found."));

        // Decrease quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(1));
        borrowRecord.setFine(BigDecimal.ZERO);
        borrowRecord.setBorrowStatus(BorrowStatus.BORROWED);

        borrowRecordRepository.save(borrowRecord);
    }

    public void returnBook(Long borrowId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
            .orElseThrow(() -> new IllegalStateException("Borrow record not found."));

        record.setBorrowStatus(BorrowStatus.RETURNED);
        borrowRecordRepository.save(record);
    }

    public List<ResponseRecord> getUserRecords(UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        fineService.updateFineForUser(user.getId());

        return borrowRecordRepository.findByUserId(user.getId()).stream()
            .map(mapper::toResponse)
            .toList();

    }

    @Transactional
    public void confirmReturn(ReturnBookRequest request) {
        BorrowRecord record = borrowRecordRepository.findById(request.id())
                .orElseThrow(() -> new IllegalStateException("Returning invalid borrow."));

        Books book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        record.setReturnDate(LocalDate.now());
        record.setBorrowStatus(BorrowStatus.RETURNCONFIRMED);
        record.setFine(fineService.calculateFine(record));
        borrowRecordRepository.save(record);
    }

    public List<ResponseRecord> getReturnedBookUserRecords() {
        return borrowRecordRepository.findAll().stream()
            .map(mapper::toResponse)
            .toList();
    }
}