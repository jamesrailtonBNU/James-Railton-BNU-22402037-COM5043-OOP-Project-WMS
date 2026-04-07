package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FinancialTransaction {

    private final int transactionId;
    private final LocalDateTime transactionDate;
    private final BigDecimal transactionAmount;
    private final BigDecimal paymentMethod;
    private final PaymentType paymentType;
    private final TransactionType transactionType;


    FinancialTransaction(int transactionId, LocalDateTime transactionDate, BigDecimal transactionAmount, BigDecimal paymentMethod, PaymentType paymentType, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.paymentMethod = paymentMethod;
        this.paymentType = paymentType;
        this.transactionType = transactionType;
    }

}
