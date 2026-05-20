package com.debttrackr.service.dto;

import com.debttrackr.domain.enumeration.PaymentMode;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private Long personId;

    private String transactionId; // OPTIONAL

    private BigDecimal amount;

    private PaymentMode paymentMode;

    private String paymentReference;

    private String remarks;
    private String provider;
    private String paymentIdentifier;


}
