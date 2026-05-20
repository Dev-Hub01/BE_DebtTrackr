package com.debttrackr.domain;
import com.debttrackr.domain.enumeration.PaymentMode;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    // 🔗 Who made the payment
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "person_id", nullable = false)
//    private Person person;

    // 🔗 Which transaction this payment belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionRecord transaction;

    // 💰 Amount paid
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    // 💳 Mode of payment
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMode paymentMode; // UPI, BANK, CASH

    // 🧾 Payment reference (UPI ref / bank txn id)
    private String paymentReference;

    // 🏦 Optional payment identifier (UPI ID / account)
    private String paymentIdentifier;
    // ex: mono@ybl OR masked acc no

    // 🏢 Optional provider
    private String provider;
    // GPay, PhonePe, SBI

    // 📅 When payment actually happened
    private LocalDateTime paymentDate;

    // 📝 Notes
    private String remarks;

    // ⏱ Audit
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    private Long personId;
}
