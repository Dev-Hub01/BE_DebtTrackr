package com.debttrackr.repository;

import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionRecord, String> {

    List<TransactionRecord> findByPersonIdAndStatusNotOrderByTransactionDateAsc(Long personId, TransactionStatus status);
        List<TransactionRecord> findByPersonIdAndStatusInOrderByCreatedAtAsc(Long personId, List<TransactionStatus> status);
    List<TransactionRecord> findAllByOrderByTransactionDateDesc();

    List<TransactionRecord> findByPersonIdOrderByTransactionDateDesc(Long personId);

    List<TransactionRecord> findByStatusOrderByTransactionDateDesc(TransactionStatus status);
    List<TransactionRecord> findByStatusIn(List<TransactionStatus> statuses);

//
}
