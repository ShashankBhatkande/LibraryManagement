package com.librarysystem.borrow.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record ResponseRecord(Long id, String title, BigDecimal fine, boolean paidFine, String borrowStatus,
        LocalDate borrowDate, LocalDate dueDate,
        LocalDate returnDate) {

}
