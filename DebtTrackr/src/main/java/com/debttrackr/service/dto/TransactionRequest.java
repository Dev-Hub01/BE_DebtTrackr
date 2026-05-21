package com.debttrackr.service.dto;

import com.debttrackr.domain.enumeration.Category;
import com.debttrackr.domain.enumeration.PaymentMode;
import com.debttrackr.domain.enumeration.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    private Long fromPersonId;
    private Long toPersonId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate transactionDate;
    private TransactionType type; // LEND / BORROW
    private String remarks;
    private Category category;
    private PaymentMode paymentMode;
    private String transactionNo;
}
