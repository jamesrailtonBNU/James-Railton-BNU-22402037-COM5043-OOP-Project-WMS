package model;

import java.time.LocalDateTime;

public class ItemMovementRecord {

    private final MovementType movementType;
    private final int quantity;
    private final String note;
    private final LocalDateTime timestamp;

    public ItemMovementRecord(MovementType movementType, int quantity, String note) {
        this.movementType = movementType;
        this.quantity = quantity;
        this.note = note == null ? "" : note.trim();
        this.timestamp = LocalDateTime.now();
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
