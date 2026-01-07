package com.librarysystem.borrow.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.librarysystem.borrow.model.BorrowRecord;
import com.librarysystem.borrow.model.BorrowStatus;
import com.librarysystem.borrow.repository.BorrowRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FineService {
    private final BorrowRecordRepository borrowRecordRepository;
    private static final BigDecimal finePerDay = BigDecimal.valueOf(50);

    public BigDecimal getTotalFine(Long userId) {
        updateFineForUser(userId);
        return borrowRecordRepository.getTotalFine(userId);
    }

    public BigDecimal calculateFine(BorrowRecord record) {
        if (record.getReturnDate() == null && 
                LocalDate.now().isAfter(record.getDueDate())) {
            long overDueDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            return finePerDay.multiply(BigDecimal.valueOf(overDueDays));
        }
        return BigDecimal.ZERO;
    }

    public void updateFineForUser(Long userId) {
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
}
