package com.librarysystem.borrow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
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
    public Map<String, String> borrowBook(@RequestBody BorrowRequest borrowRequest) {
        service.borrowBook(borrowRequest);
        return Map.of("message", "Book borrowed successfully.");
    }

    @PatchMapping("/return")
    public Map<String, String> returnBook(@RequestBody ReturnBookRequest returnBookRequest) {
        service.returnBook(returnBookRequest.id());
        return Map.of("message", "Book returned successfully.");
    }

    @GetMapping("/user-records")
    public List<ResponseRecord> getUserRecords(@AuthenticationPrincipal UserDetails userDetails) {
        return service.getUserRecords(userDetails);
    }

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<ResponseRecord> getRecords() {
        return service.getReturnedBookUserRecords();
    }

    @PatchMapping("/confirm-return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public Map<String, String> confirmReturn(@RequestBody ReturnBookRequest request) {
        service.confirmReturn(request);
        return Map.of("message", "Book returned confirmed.");
    }
}
