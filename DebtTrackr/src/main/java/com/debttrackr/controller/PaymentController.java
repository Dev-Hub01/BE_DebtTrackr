package com.debttrackr.controller;

import com.debttrackr.service.PaymentService;
import com.debttrackr.service.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Make a payment
     *
     * Case 1:
     * If transactionId is provided → payment applied to that specific transaction
     *
     * Case 2:
     * If transactionId is null → payment auto-distributed using FIFO
     */
    @PostMapping
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequest request) {

        paymentService.makePayment(request);

        return ResponseEntity.ok("Payment processed successfully");
    }
}
