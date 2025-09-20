package com.librarysystem.borrow.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.librarysystem.book.model.Books;
import com.librarysystem.book.repository.BookRepository;
import com.librarysystem.borrow.dto.ResponseRecord;
import com.librarysystem.borrow.model.BorrowRecord;
import com.librarysystem.borrow.model.BorrowStatus;
import com.librarysystem.borrow.repository.BorrowRecordRepository;
import com.librarysystem.user.model.User;
import com.librarysystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public boolean checkAvailability(Long bookId) {
        Optional<Books> book = bookRepository.findById(bookId);
        return book.get().getQuantity() > 0;
    }

    public BigDecimal getTotalFine(Long userId) {
        return borrowRecordRepository.getTotalFine(userId);
    }

    public int getNumberOfBorrowedBooks(Long userId) {
        return borrowRecordRepository.getNumberOfBorrowedBooks(userId);
       
    }

    public void borrowBook(Long bookId, Long userId) {
        //Decrease quantity
        Books book = bookRepository.findById(bookId).get();
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        updateFine(userId);
        User user = userRepository.findById(userId).get();
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(30));
        borrowRecord.setFine(BigDecimal.ZERO);
        borrowRecord.setBorrowStatus(BorrowStatus.BORROWED);

        borrowRecordRepository.save(borrowRecord);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public BigDecimal calculateFine(BorrowRecord record) {
        if(LocalDate.now().isAfter(record.getDueDate())) {
            long overDueDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            return BigDecimal.valueOf(overDueDays * 50);
        }
        return BigDecimal.ZERO;
    }

    public void updateFine(Long userId) {
        List<BorrowRecord> records = borrowRecordRepository.findByUserId(userId);
        for(BorrowRecord currRecord: records) {
            if(currRecord.getReturnDate() == null) {
                if(LocalDate.now().isAfter(currRecord.getDueDate())) {
                    currRecord.setFine(calculateFine(currRecord));
                    currRecord.setBorrowStatus(BorrowStatus.OVERDUE);
                } else if(!LocalDate.now().isAfter(currRecord.getDueDate())) {
                    currRecord.setFine(BigDecimal.ZERO);
                }
            } 
            borrowRecordRepository.save(currRecord);
        }
    }

    public void returnBook(Long userId, Long bookId) {
        BorrowRecord record = borrowRecordRepository.findByUserIdAndBookIdAndReturnDateIsNull(userId, bookId);

        record.setFine(calculateFine(record));
        record.setBorrowStatus(BorrowStatus.RETURNED);
        record.setReturnDate(LocalDate.now());

        borrowRecordRepository.save(record);
    }

    public List<ResponseRecord> getUserRecords(Long id) {
        return borrowRecordRepository.findByUserId(id).stream()
        .map(record -> ResponseRecord.builder()
            .title(record.getBook().getTitle())
            .borrowStatus(record.getBorrowStatus().name())
            .borrowDate(record.getBorrowDate())
            .returnDate(record.getReturnDate())
            .dueDate(record.getDueDate())
            .fine(record.getFine())
            .paidFine(record.isFinePaid())
            .build()
        ).toList();
            
    }
}