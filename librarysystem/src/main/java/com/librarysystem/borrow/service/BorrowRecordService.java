package com.librarysystem.borrow.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.librarysystem.book.model.Books;
import com.librarysystem.book.repository.BookRepository;
import com.librarysystem.borrow.dto.BorrowRequest;
import com.librarysystem.borrow.dto.ResponseRecord;
import com.librarysystem.borrow.dto.ReturnBookRequest;
import com.librarysystem.borrow.model.BorrowRecord;
import com.librarysystem.borrow.model.BorrowStatus;
import com.librarysystem.borrow.repository.BorrowRecordRepository;
import com.librarysystem.user.model.User;
import com.librarysystem.user.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Optional<BorrowRecord> getBorrowRecord(Long id) {
        return borrowRecordRepository.findById(id);
    }

    private boolean checkAvailability(Long bookId) {
        Optional<Books> book = bookRepository.findById(bookId);
        return book.get().getQuantity() > 0;
    }

    private BigDecimal getTotalFine(Long userId) {
        updateFine(userId);
        return borrowRecordRepository.getTotalFine(userId);
    }

    private int getNumberOfBorrowedBooks(Long userId) {
        return borrowRecordRepository.countByUserIdAndBorrowStatusIn(userId,
                List.of(BorrowStatus.BORROWED, BorrowStatus.OVERDUE, BorrowStatus.RETURNED));

    }

    public void borrowBook(BorrowRequest request) {
        String email = request.username();
        Long bookId = request.bookId();

        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book is not present."));
        User user = findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        Long userId = user.getId();

        boolean available = checkAvailability(bookId);
        BigDecimal fine = getTotalFine(userId);
        int noBorrowedBooks = getNumberOfBorrowedBooks(userId);

        if (!available) {
            throw new IllegalStateException("Book is out of stock.");
        } else if (fine.compareTo(new BigDecimal("500")) > 0) {
            throw new IllegalStateException("Please pay existing fine before borrowing.");
        } else if (noBorrowedBooks >= 5) {
            throw new IllegalStateException("You can not borrow more than 5 books.");
        }

        // Decrease quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        updateFine(userId);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(1));
        borrowRecord.setFine(BigDecimal.ZERO);
        borrowRecord.setBorrowStatus(BorrowStatus.BORROWED);

        borrowRecordRepository.save(borrowRecord);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private BigDecimal calculateFine(BorrowRecord record) {
        if (LocalDate.now().isAfter(record.getDueDate())) {
            long overDueDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            return BigDecimal.valueOf(overDueDays * 50);
        }
        return BigDecimal.ZERO;
    }

    private void updateFine(Long userId) {
        List<BorrowRecord> records = borrowRecordRepository.findByUserId(userId);
        for (BorrowRecord currRecord : records) {
            if (currRecord.getReturnDate() == null) {
                if (LocalDate.now().isAfter(currRecord.getDueDate())) {
                    currRecord.setFine(calculateFine(currRecord));
                    if(!currRecord.getBorrowStatus().equals(BorrowStatus.RETURNED))currRecord.setBorrowStatus(BorrowStatus.OVERDUE);
                } else if (!LocalDate.now().isAfter(currRecord.getDueDate())) {
                    currRecord.setFine(BigDecimal.ZERO);
                }
            }
            borrowRecordRepository.save(currRecord);
        }
    }

    public void returnBook(Long borrowId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId).get();

        record.setBorrowStatus(BorrowStatus.RETURNED);
        borrowRecordRepository.save(record);
    }

    public List<ResponseRecord> getUserRecords(UserDetails userDetails) {
        String email = userDetails.getUsername();
        Long userId = findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found."))
                .getId();

        updateFine(userId);
        return borrowRecordRepository.findByUserId(userId).stream()

                .map(record -> ResponseRecord.builder()
                        .id(record.getId())
                        .title(record.getBook().getTitle())
                        .borrowStatus(record.getBorrowStatus().name())
                        .borrowDate(record.getBorrowDate())
                        .returnDate(record.getReturnDate())
                        .dueDate(record.getDueDate())
                        .fine(record.getFine())
                        .paidFine(record.isFinePaid())
                        .build())
                .toList();

    }

    public void confirmReturn(ReturnBookRequest request) {
        BorrowRecord record = getBorrowRecord(request.id())
                .orElseThrow(() -> new IllegalStateException("Returning invalid borrow."));

        Long bookId = record.getBook().getId();

        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Returning invalid book."));
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        record.setReturnDate(LocalDate.now());
        record.setBorrowStatus(BorrowStatus.RETURNCONFIRMED);
        record.setFine(calculateFine(record));
        borrowRecordRepository.save(record);
    }

    public List<ResponseRecord> getReturnedBookUserRecords() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        for(BorrowRecord record: records) {
            updateFine(record.getUser().getId());
        }

        return borrowRecordRepository.findAll().stream()

                .map(record -> ResponseRecord.builder()
                        .id(record.getId())
                        .title(record.getBook().getTitle())
                        .borrowStatus(record.getBorrowStatus().name())
                        .borrowDate(record.getBorrowDate())
                        .returnDate(record.getReturnDate())
                        .dueDate(record.getDueDate())
                        .fine(record.getFine())
                        .paidFine(record.isFinePaid())
                        .build())
                .toList();
    }
}