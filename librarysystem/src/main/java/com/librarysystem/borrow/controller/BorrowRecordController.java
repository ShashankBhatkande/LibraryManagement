package com.librarysystem.borrow.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.borrow.dto.BorrowRequest;
import com.librarysystem.borrow.dto.ResponseRecord;
import com.librarysystem.borrow.dto.ReturnBookRequest;
import com.librarysystem.borrow.model.BorrowRecord;
import com.librarysystem.borrow.service.BorrowRecordService;
import com.librarysystem.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@Slf4j
public class BorrowRecordController {
    private final BorrowRecordService service;

    @PatchMapping("/borrow")
    public ResponseEntity<Map<String, String>> borrowBook(@RequestBody BorrowRequest borrowRequest) {
        String email = borrowRequest.username();
        Long bookId = borrowRequest.bookId();

        Optional<User> user =  service.findByEmail(email);
        if(!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "No user found."));
        }

        Long userId = user.get().getId();
        boolean available = service.checkAvailability(bookId);
        BigDecimal fine = service.getTotalFine(userId);
        int noBorrowedBooks = service.getNumberOfBorrowedBooks(userId);

        if(available && fine.compareTo(new BigDecimal("500")) <= 0 && noBorrowedBooks <= 5) {
            service.borrowBook(bookId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book Borrowed Successfully."));
        } else if(fine.compareTo(new BigDecimal("500")) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Please pay existing fine before borrowing."));
        } else if(!available) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Book is out of stock."));
        } else if(noBorrowedBooks > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","You have borrowed more than 5 books."));
        } 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Borrow request failed."));
    }

    @PatchMapping("/returnBook")
    public ResponseEntity<Map<String, String>> returnBook(@RequestBody ReturnBookRequest returnBookRequest) {
        log.info("Borrow Record id is: " + returnBookRequest.id());
        BorrowRecord borrowRecord = service.getBorrowRecord(returnBookRequest.id());
        Long bookId = borrowRecord.getBook().getId();
        String username = borrowRecord.getUser().getEmail();
        User user = service.findByEmail(username).get();

        service.returnBook(user.getId(), bookId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book returned successfully."));
    }

    @GetMapping("/getUserRecords")
    public List<ResponseRecord> getUserRecords(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Long id = service.findByEmail(email).get().getId();
        return service.getUserRecords(id);
    } 
    @GetMapping("/getRecords")
    public List<ResponseRecord> getRecords() {
        return service.getReturnedBookUserRecords();
    } 
    @PatchMapping("/confirmReturn")
    public ResponseEntity<Map<String, String>> confirmReturn(@RequestBody ReturnBookRequest request) {
        BorrowRecord record = service.getBorrowRecord(request.id());
        Long bookId = record.getBook().getId();
        String username = record.getUser().getEmail();
        User user = service.findByEmail(username).get();

        service.confirmReturn(user.getId(), bookId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book returned confirmed."));
    }
}
