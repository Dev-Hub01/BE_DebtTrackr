package com.debttrackr.domain;

import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.domain.enumeration.TransactionType;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction_records")
public class TransactionRecord {

    @Id
    private String transactionId;

    // ── WHO ─────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // ── TYPE ────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // LEND / BORROW

    // ── MONEY ───────────────────────────────────
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(precision = 15, scale = 2)
    private BigDecimal amountRepaid = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate = BigDecimal.ZERO;

    // ── DATES ───────────────────────────────────
    @Column(nullable = false)
    private LocalDate transactionDate;

//    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate extendedDueDate;

    // ── STATUS ──────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    // ── REMINDERS ───────────────────────────────
    private boolean reminderSent = false;

    private int reminderCount = 0;

    private LocalDateTime lastReminderSentAt;

    // ── EXTRA ───────────────────────────────────
    private String purpose;

    @Column(length = 2000)
    private String remarks;

    private Boolean active;

    // ── AUDIT ───────────────────────────────────
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ── DERIVED METHODS (IMPORTANT) ─────────────

    public BigDecimal getBalanceAmount() {
        return amount.subtract(amountRepaid);
    }

    public LocalDate getEffectiveDueDate() {
        return (extendedDueDate != null) ? extendedDueDate : dueDate;
    }

    public boolean isOverdue() {
        return !TransactionStatus.COMPLETED.equals(status)
                && !TransactionStatus.CANCELLED.equals(status)
                && getEffectiveDueDate().isBefore(LocalDate.now());
    }

    // ── AUTO STATUS UPDATE ──────────────────────
    @PrePersist
    @PreUpdate
    public void updateStatus() {
        BigDecimal balance = getBalanceAmount();

        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            this.status = TransactionStatus.COMPLETED;
        } else if (amountRepaid.compareTo(BigDecimal.ZERO) > 0) {
            this.status = TransactionStatus.PARTIAL;
        } else {
            this.status = TransactionStatus.PENDING;
        }
    }
}
