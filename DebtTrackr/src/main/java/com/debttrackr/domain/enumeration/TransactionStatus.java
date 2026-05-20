package com.debttrackr.domain.enumeration;

/**
 * Lifecycle status of a lend or borrow transaction.
 */
public enum TransactionStatus {

    /** Money has been given/taken; nothing repaid yet. */
    PENDING,

    /** Partially repaid; balance still outstanding. */
    PARTIAL,

    /** Fully repaid; transaction closed. */
    COMPLETED,

    /** Due date passed without full repayment. */
    OVERDUE,

    /** Extended to a new due date. */
    EXTENDED,

    /** Written off / forgiven. */
    CANCELLED
}


