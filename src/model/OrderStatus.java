package model;
// this is used for tracking the status of an order as it moves through the supply chain process.

public enum OrderStatus {
    PENDING,
    IN_TRANSIT,
    PROCESSED,
    DELIVERED,
    DELAYED,
    CANCELLED
}
