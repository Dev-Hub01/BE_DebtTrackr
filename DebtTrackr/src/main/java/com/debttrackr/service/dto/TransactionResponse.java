package com.debttrackr.service.dto;

import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.domain.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String transactionId;

    private Long personId;
    private String personName;

    private BigDecimal amount;
    private BigDecimal amountRepaid;
    private BigDecimal balanceAmount;

    private TransactionType type;

    private LocalDate transactionDate;
    private LocalDate dueDate;

    private TransactionStatus status;

    private String remarks;
}
