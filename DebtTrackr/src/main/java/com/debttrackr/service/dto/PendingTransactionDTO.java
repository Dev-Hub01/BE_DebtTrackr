package com.debttrackr.service.dto;

import com.debttrackr.domain.enumeration.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingTransactionDTO {

    private String transactionId;
    private BigDecimal totalAmount;
    private BigDecimal balanceAmount;
    private LocalDate dueDate;
    private String remarks;
    private TransactionStatus status;
    private BigDecimal amountPaid;
    private boolean overdue;
    private LocalDate tnxDate;
}
