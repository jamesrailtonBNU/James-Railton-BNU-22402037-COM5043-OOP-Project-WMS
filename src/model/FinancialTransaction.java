package model;
// this is used for both payment and refund transactions, as they share similar attributes and behaviors. The transaction type can be used to differentiate between the two when necessary.

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FinancialTransaction {

    private final int transactionId;
    private final int orderId;
    private final LocalDateTime transactionDateTime;
    private final BigDecimal transactionAmount;
    private final TransactionType transactionType;
    private TransactionStatus transactionStatus;


    public FinancialTransaction(int transactionId,
                                int orderId,
                                LocalDateTime transactionDateTime,
                                BigDecimal transactionAmount,
                                TransactionType transactionType) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.transactionDateTime = transactionDateTime;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionStatus = TransactionStatus.COMPLETED;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int orderId() {
        return orderId;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public boolean isActive() {
        return transactionStatus == TransactionStatus.COMPLETED;
    }

    public boolean voidTransaction() {
        if (!isActive()) {
            return false;
        }

        transactionStatus = TransactionStatus.VOIDED;
        return true;
    }
}
