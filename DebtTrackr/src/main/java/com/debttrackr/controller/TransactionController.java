package com.debttrackr.controller;

import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.service.TransactionService;
import com.debttrackr.service.dto.TransactionRequest;
import com.debttrackr.service.dto.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {


    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    // ✅ Get all transactions
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // ✅ Get by person
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<TransactionResponse>> getByPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(transactionService.getTransactionsByPerson(personId));
    }

    // ✅ Get by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionResponse>> getByStatus(@PathVariable TransactionStatus status) {
        return ResponseEntity.ok(transactionService.getTransactionsByStatus(status));
    }

}
