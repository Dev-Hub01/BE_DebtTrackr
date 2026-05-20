package com.debttrackr.service.dto;

import com.debttrackr.domain.enumeration.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    private Long personId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private TransactionType type; // LEND / BORROW
    private String remarks;
    private boolean mailReq;
}
