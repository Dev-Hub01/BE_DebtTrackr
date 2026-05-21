package com.debttrackr.controller;


import com.debttrackr.domain.enumeration.TransactionType;
import com.debttrackr.service.TransactionImportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TransactionImportController {



    @Autowired
    private TransactionImportService transactionImportService;

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file, @RequestParam Long personId, @RequestParam TransactionType type) {
        transactionImportService.importTransactions(file, personId, type);
        return ResponseEntity.ok("Transactions imported successfully");
    }
}
