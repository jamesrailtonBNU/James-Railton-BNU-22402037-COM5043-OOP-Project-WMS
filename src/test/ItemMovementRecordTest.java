package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.ItemMovementRecord;
import model.MovementType;
import static org.junit.jupiter.api.Assertions.*;

class ItemMovementRecordTest {

    private ItemMovementRecord movementRecord;

    @BeforeEach
    void setUp() {
        movementRecord = new ItemMovementRecord(MovementType.ADD, 10, "ISS resupply");
    }

    @Test
    void getMovementType() {
        assertEquals(MovementType.ADD, movementRecord.getMovementType());
    }

    @Test
    void getQuantity() {
        assertEquals(10, movementRecord.getQuantity());
    }

    @Test
    void getNote() {
        assertEquals("ISS resupply", movementRecord.getNote());
    }

    @Test
    void getTimestamp() {
        assertNotNull(movementRecord.getTimestamp());
    }
}