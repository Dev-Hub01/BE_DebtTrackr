package com.debttrackr.service;

import com.debttrackr.domain.IdGenerator;
import com.debttrackr.domain.Person;
import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.domain.enumeration.TransactionType;
import com.debttrackr.repository.PersonRepository;
import com.debttrackr.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionImportService {

    private final TransactionRepository transactionRepository;
    private final PersonRepository personRepository;

    public void importTransactions(MultipartFile file, Long personId, TransactionType type) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            List<TransactionRecord> transactions = reader.lines()
                    .skip(1) // skip header
                    .map(line -> mapToTransaction(line, person, type))
                    .toList();

            transactionRepository.saveAll(transactions);

        } catch (Exception e) {
            throw new RuntimeException("Failed to process CSV", e);
        }
    }

    private TransactionRecord mapToTransaction(String line, Person person, TransactionType type) {

        String[] fields = line.split(",");

        LocalDate date = LocalDate.parse(fields[0].trim());
        BigDecimal amount = new BigDecimal(fields[1].trim());
        String paymentMode = fields[2].trim();

        TransactionRecord txn = new TransactionRecord();

        txn.setTransactionId(IdGenerator.generateId()); // your custom TXID
        txn.setPerson(person);
        txn.setAmount(amount);
        txn.setAmountRepaid(BigDecimal.ZERO);
        txn.setType(type);
        txn.setStatus(TransactionStatus.PENDING);
        txn.setCreatedAt(date.atStartOfDay());
        txn.setTransactionDate(date);

        // optional
        txn.setRemarks("Imported from CSV - " + paymentMode);

        return txn;
    }

}
