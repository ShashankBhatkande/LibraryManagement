package com.librarysystem.borrow.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.librarysystem.borrow.model.BorrowRecord;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long>{
    @Query("SELECT COALESCE(SUM(br.fine), 0) FROM BorrowRecord br WHERE br.user.id = :userId AND br.finePaid IS FALSE")
    BigDecimal getTotalFine(@Param("userId") Long userId);

    @Query("SELECT COUNT(*) FROM BorrowRecord br WHERE br.user.id = :userId")
    int getNumberOfBorrowedBooks(@Param("userId") Long userId);

    List<BorrowRecord> findByUserId(Long userId);
    BorrowRecord findByUserIdAndBookIdAndReturnDateIsNull(Long userId, Long bookId);
}
