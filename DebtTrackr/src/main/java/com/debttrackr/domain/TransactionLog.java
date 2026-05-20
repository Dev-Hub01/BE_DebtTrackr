package com.debttrackr.domain;


import com.debttrackr.domain.enumeration.OperationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable audit log entry for a lend or borrow record.
 * Every status change, partial payment, or extension creates a new row here.
 * Never update or delete rows — append only.
 */
@Entity
@Table(name = "transaction_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business ID of the transaction (TransactionRecord.transactionId)
     *
     * Example:
     * TXID20260331-A1B2C3D4
     *
     * Used to link this log to a specific transaction
     */
    @Column(name = "entity_id")
    private String entityId;

    /**
     * Type of operation performed on transaction
     *
     * Example values:
     * PAYMENT
     * STATUS_CHANGE
     * REMINDER
     * EMAIL
     * CREATED
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    /**
     * Specific action performed (more detailed than operationType)
     *
     * Example values:
     * "LEND_CREATED"
     * "PARTIAL_PAYMENT"
     * "FULL_PAYMENT"
     * "STATUS_UPDATED"
     * "REMINDER_SENT"
     * "EMAIL_SENT"
     */
    @Column(nullable = false)
    private String action;

    /**
     * Previous status before this action
     *
     * Example:
     * PENDING
     * PARTIAL
     *
     * Null when transaction is newly created
     */
    private String previousStatus;

    /**
     * New status after this action
     *
     * Example:
     * PARTIAL
     * COMPLETED
     *
     * Helps track status transitions
     */
    private String newStatus;

    /**
     * Amount involved in this operation
     *
     * Example:
     * 500.00 → partial payment
     * 2000.00 → full payment
     *
     * Null for non-money actions (like reminders/emails)
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Additional details about this log
     *
     * Example:
     * "Paid via UPI"
     * "Extended due date by 5 days"
     * "Reminder sent before due date"
     */
    @Column(length = 1000)
    private String remarks;

    /**
     * Who triggered this action
     *
     * Example:
     * USER → manual action
     * SYSTEM → scheduler/auto process
     * ADMIN → future use
     */
    private String triggeredBy;

    /**
     * Timestamp when email was sent (if applicable)
     *
     * Example:
     * 2026-03-31T10:15:30
     *
     * Used only when operationType = EMAIL
     */
    private LocalDateTime emailSentAt;

    /**
     * Automatically set when log is created
     *
     * Example:
     * 2026-03-31T09:00:00
     *
     * Used for audit/history tracking
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}