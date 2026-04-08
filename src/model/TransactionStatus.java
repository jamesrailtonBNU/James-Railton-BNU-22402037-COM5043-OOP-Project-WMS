package model;
// This is used for both sales and purchase transactions, as well as returns. It indicates whether the transaction was completed successfully or if it was voided (canceled).

public enum TransactionStatus {
    COMPLETED,
    VOIDED
}
