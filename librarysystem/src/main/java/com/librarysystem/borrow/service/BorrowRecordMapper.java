package com.librarysystem.borrow.service;

import org.springframework.stereotype.Component;
import com.librarysystem.borrow.dto.ResponseRecord;
import com.librarysystem.borrow.model.BorrowRecord;

@Component
public class BorrowRecordMapper {
    public ResponseRecord toResponse(BorrowRecord record) {
        return ResponseRecord.builder()
                .id(record.getId())
                .title(record.getBook().getTitle())
                .borrowStatus(record.getBorrowStatus().name())
                .borrowDate(record.getBorrowDate())
                .returnDate(record.getReturnDate())
                .dueDate(record.getDueDate())
                .fine(record.getFine())
                .paidFine(record.isFinePaid())
                .build();
    }
}
