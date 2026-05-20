package com.debttrackr.service;

import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.service.dto.PaymentRequest;
import com.debttrackr.service.dto.TransactionRequest;
import com.debttrackr.service.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest request);

    void makePayment(PaymentRequest request);

    List<TransactionResponse> getAllTransactions();

    List<TransactionResponse> getTransactionsByPerson(Long personId);

    List<TransactionResponse> getTransactionsByStatus(TransactionStatus status);
}
