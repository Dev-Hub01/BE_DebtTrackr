package com.debttrackr.repository;

import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.domain.enumeration.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionRecord, String> {

    List<TransactionRecord> findByPersonIdAndStatusNotOrderByTransactionDateAsc(Long personId, TransactionStatus status);
        List<TransactionRecord> findByPersonIdAndStatusInOrderByCreatedAtAsc(Long personId, List<TransactionStatus> status);
    List<TransactionRecord> findAllByOrderByTransactionDateDesc();

    List<TransactionRecord> findByPersonIdOrderByTransactionDateDesc(Long personId);

    List<TransactionRecord> findByStatusOrderByTransactionDateDesc(TransactionStatus status);
    List<TransactionRecord> findByStatusIn(List<TransactionStatus> statuses);





    @Query(value = """
SELECT
    YEAR(transaction_date)  AS year,
    DATE_FORMAT(transaction_date, '%b') AS month,
    SUM(amount)             AS monthlyAmount,
    SUM(SUM(amount)) OVER() AS totalAmount,
    SUM(SUM(amount_repaid)) OVER() AS settledAmount,
    SUM(amount) OVER()  - SUM(amount_repaid) OVER() AS totalPending
FROM transaction_records
WHERE type = :type
AND status <> 'CANCELLED'
GROUP BY YEAR(transaction_date),
         DATE_FORMAT(transaction_date, '%b')
ORDER BY year, month;
""", nativeQuery = true)
    List<DashboardProjection> getTransactionDataSummary(String type);
//


     interface DashboardProjection {

        String getYear();
        String getMonth();

        BigDecimal getMonthlyAmount();
         BigDecimal getSettledAmount();
         BigDecimal getTotalAmount();

        BigDecimal getTotalPending();

    }

    List<TransactionRecord>
    findTop5ByTypeOrderByTransactionDateDesc(TransactionType type);
}
