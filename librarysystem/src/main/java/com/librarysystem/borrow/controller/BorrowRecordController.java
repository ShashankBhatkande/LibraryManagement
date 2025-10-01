package com.librarysystem.borrow.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
import com.librarysystem.borrow.service.BorrowRecordService;

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
        try {
            service.borrowBook(borrowRequest);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book borrowed successfully."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @PatchMapping("/returnBook")
    public ResponseEntity<Map<String, String>> returnBook(@RequestBody ReturnBookRequest returnBookRequest) {
        try {
            service.returnBook(returnBookRequest.id());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book returned successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong"));
        }
    }

    @GetMapping("/getUserRecords")
    public ResponseEntity<?> getUserRecords(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<ResponseRecord> record = service.getUserRecords(userDetails);
            return ResponseEntity.ok(record);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }
    }

    @GetMapping("/getRecords")
    public ResponseEntity<?> getRecords() {
        try {
            List<ResponseRecord> record = service.getReturnedBookUserRecords();
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong"));
        }
    }

    @PatchMapping("/confirmReturn")
    public ResponseEntity<Map<String, String>> confirmReturn(@RequestBody ReturnBookRequest request) {
        try {
            service.confirmReturn(request);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Book returned confirmed."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong."));
        }

    }
}
