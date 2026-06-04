package com.debttrackr.service;

import com.debttrackr.domain.IdGenerator;
import com.debttrackr.domain.Person;
import com.debttrackr.domain.TransactionLog;
import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.OperationType;
import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.mapper.TransactionMapper;
import com.debttrackr.repository.PersonRepository;
import com.debttrackr.repository.TransactionLogRepository;
import com.debttrackr.repository.TransactionRepository;
import com.debttrackr.service.dto.PaymentRequest;
import com.debttrackr.service.dto.TransactionRequest;
import com.debttrackr.service.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final TransactionLogRepository logRepo;
    private final PersonRepository personRepo;
    private final EmailService emailService;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponse createTransaction(TransactionRequest request) {



        Person person = personRepo.findById(request.getPersonId())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        TransactionRecord txn = new TransactionRecord();
        txn.setTransactionId(IdGenerator.generateId()); // your TXID logic
        txn.setPerson(person);
        txn.setAmount(request.getAmount());
        txn.setAmountRepaid(BigDecimal.ZERO);
        txn.setTransactionDate(LocalDate.now());
        txn.setDueDate(request.getDueDate());
        txn.setType(request.getType());
        txn.setStatus(TransactionStatus.PENDING);
        txn.setReminderCount(1);
        txn.setPaymentMode(request.getPaymentMode());
        txn.setCategory(request.getCategory());
        txn.setTransactionNo(request.getTransactionNo());
        txn.setUser_id(request.getUserId());

        TransactionRecord transactionRecord = transactionRepo.save(txn);
        TransactionResponse transactionResponse = transactionMapper.toDto(transactionRecord);


        // 🔥 Log creation
        TransactionLog log = new TransactionLog();
        log.setEntityId(txn.getTransactionId());
        log.setOperationType(OperationType.CREATED);
        log.setAction("TRANSACTION_CREATED");
        log.setAmount(request.getAmount());
        log.setNewStatus("PENDING");
        log.setTriggeredBy("USER");


        logRepo.save(log);

//        if(request.isMailReq()){
          emailService.notifyTransaction(transactionResponse);
//        }

        return mapToResponse(txn);
    }

    // 🔥 FIFO Payment Logic
    @Override
    public void makePayment(PaymentRequest request) {

        List<TransactionRecord> txns =
                transactionRepo.findByPersonIdAndStatusNotOrderByTransactionDateAsc(
                        request.getPersonId(),
                        TransactionStatus.COMPLETED
                );

        BigDecimal remaining = request.getAmount();

        for (TransactionRecord txn : txns) {

            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal balance = txn.getBalanceAmount();
            String prevStatus = txn.getStatus().name();

            if (remaining.compareTo(balance) >= 0) {

                // Full payment
                txn.setAmountRepaid(txn.getAmount());
                txn.setStatus(TransactionStatus.COMPLETED);

                remaining = remaining.subtract(balance);

                saveLog(txn, balance, prevStatus, "COMPLETED", "FULL_PAYMENT");

            } else {

                // Partial payment
                txn.setAmountRepaid(txn.getAmountRepaid().add(remaining));
                txn.setStatus(TransactionStatus.PARTIAL);

                saveLog(txn, remaining, prevStatus, "PARTIAL", "PARTIAL_PAYMENT");

                remaining = BigDecimal.ZERO;
            }

            transactionRepo.save(txn);
        }
    }

    private void saveLog(TransactionRecord txn, BigDecimal amount,
                         String prevStatus, String newStatus, String action) {

        TransactionLog log = new TransactionLog();
        log.setEntityId(txn.getTransactionId());
        log.setOperationType(OperationType.PAYMENT);
        log.setAction(action);
        log.setAmount(amount);
        log.setPreviousStatus(prevStatus);
        log.setNewStatus(newStatus);
        log.setTriggeredBy("USER");

        logRepo.save(log);
    }


    private TransactionResponse mapToResponse(TransactionRecord txn) {

        return TransactionResponse.builder()
                .transactionId(txn.getTransactionId())
//                .personId(txn.getPerson().getId())
//                .personName(txn.getPerson().getName())
                .amount(txn.getAmount())
                .amountRepaid(txn.getAmountRepaid())
                .balanceAmount(txn.getBalanceAmount())
                .type(txn.getType())
                .transactionDate(txn.getTransactionDate())
                .dueDate(txn.getDueDate())
                .status(txn.getStatus())
                .remarks(txn.getRemarks())
                .build();
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {

        return transactionRepo.findAllByOrderByTransactionDateDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public List<TransactionResponse> getTransactionsByPerson(Long personId) {

        return transactionRepo.findByPersonIdOrderByTransactionDateDesc(personId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public List<TransactionResponse> getTransactionsByStatus(TransactionStatus status) {

        return transactionRepo.findByStatusOrderByTransactionDateDesc(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


/*
    public List<TransactionResponse>
    getTransactions(Long loggedInUserId) {

        List<TransactionRecord> transactions =
                repository.getUserTransactions(
                        loggedInUserId
                );

        return transactions.stream()

                .map(transaction -> {

                    boolean isOwner =
                            transaction
                                    .getOwnerUser()
                                    .getId()
                                    .equals(loggedInUserId);

                    TransactionType displayType =
                            transformType(
                                    transaction.getType(),
                                    isOwner
                            );

                    TransactionResponse response =
                            new TransactionResponse();

                    response.setId(
                            transaction.getId()
                    );

                    response.setPersonName(
                            transaction
                                    .getPerson()
                                    .getName()
                    );

                    response.setType(displayType);

                    response.setAmount(
                            transaction.getAmount()
                    );

                    response.setOwner(isOwner);

                    return response;
                })

                .toList();
    }

 */
}
