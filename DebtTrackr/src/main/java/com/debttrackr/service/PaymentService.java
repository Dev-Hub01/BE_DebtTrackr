package com.debttrackr.service;

import com.debttrackr.domain.PaymentDetail;
import com.debttrackr.domain.Person;
import com.debttrackr.domain.TransactionLog;
import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.OperationType;
import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.repository.PaymentRepository;
import com.debttrackr.repository.PersonRepository;
import com.debttrackr.repository.TransactionLogRepository;
import com.debttrackr.repository.TransactionRepository;
import com.debttrackr.service.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private PersonRepository personRepository;


    public void makePayment(PaymentRequest request) {

        if (request.getTransactionId() != null) {
            // ✅ DIRECT PAYMENT
            applyPaymentToSingleTransaction(request);
        } else {
            // ✅ FIFO PAYMENT
            applyPaymentFIFO(request);
        }
    }


    private void applyPaymentFIFO(PaymentRequest request) {

        BigDecimal remaining = request.getAmount();

        List<TransactionRecord> transactions = transactionRepository.findByPersonIdAndStatusInOrderByCreatedAtAsc(request.getPersonId(), Arrays.asList(TransactionStatus.PENDING, TransactionStatus.PARTIAL));

        for (TransactionRecord txn : transactions) {

            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break; // here we can throw error

            BigDecimal payAmount = remaining.min(txn.getBalanceAmount()); // here we can throw an error if access amount paid

            processPayment(txn, payAmount, request);

            remaining = remaining.subtract(payAmount);
        }
    }

    private void applyPaymentToSingleTransaction(PaymentRequest request) {

        TransactionRecord txn = transactionRepository
                .findById(request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        BigDecimal payAmount = request.getAmount();

        if (payAmount.compareTo(txn.getBalanceAmount()) > 0) {
            throw new RuntimeException("Payment exceeds balance");
        }

        processPayment(txn, payAmount, request);
    }

    private void processPayment(TransactionRecord txn,
                                BigDecimal amount,
                                PaymentRequest request) {

         personRepository.findById(request.getPersonId()).orElseThrow(() -> new RuntimeException("Person not found"));
        // 1. Save PaymentDetails
        PaymentDetail payment = new PaymentDetail();
        payment.setTransaction(txn);
        payment.setAmount(amount);
        payment.setPaymentMode(request.getPaymentMode());
        payment.setPaymentReference(request.getPaymentReference());
        payment.setRemarks(request.getRemarks());
        payment.setPaymentIdentifier(request.getPaymentIdentifier());
        payment.setProvider(request.getProvider());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPersonId(request.getPersonId());
        paymentRepository.save(payment);

        // 2. Update Transaction
        txn.setAmountRepaid(txn.getAmountRepaid().add(amount));

        if (txn.getBalanceAmount().compareTo(BigDecimal.ZERO) == 0) {
            txn.setStatus(TransactionStatus.COMPLETED);
        } else {
            txn.setStatus(TransactionStatus.PARTIAL);
        }

        transactionRepository.save(txn);

        // 3. Log
        TransactionLog log = new TransactionLog();
        log.setEntityId(txn.getTransactionId());
        log.setOperationType(OperationType.PAYMENT);
        log.setAction("PAYMENT");
        log.setAmount(amount);
        log.setTriggeredBy("USER");

        transactionLogRepository.save(log);
    }


}
