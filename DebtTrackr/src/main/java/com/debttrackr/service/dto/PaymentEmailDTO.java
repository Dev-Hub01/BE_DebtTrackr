package com.debttrackr.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEmailDTO {

    private String transactionId;
    private BigDecimal amount;
    private String paymentMode;
    private String reference;
    private LocalDateTime date;
}
