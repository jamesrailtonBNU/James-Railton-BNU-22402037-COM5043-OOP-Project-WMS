package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.InventoryItem;
import model.ItemCategory;
import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    private InventoryItem inventoryItem;

    @BeforeEach
    void setUp() {
        inventoryItem = new InventoryItem(1, "Hubble Torque Key", ItemCategory.HAND_TOOLS, 50, 20, 101);
    }

    @Test
    void getItemId() {
        assertEquals(1, inventoryItem.getItemId());
    }

    @Test
    void getItemName() {
        assertEquals("Hubble Torque Key", inventoryItem.getItemName());
    }

    @Test
    void getItemCategory() {
        assertEquals(ItemCategory.HAND_TOOLS, inventoryItem.getItemCategory());
    }

    @Test
    void getItemQuantity() {
        assertEquals(50, inventoryItem.getItemQuantity());
    }

    @Test
    void getStockQuantity() {
        assertEquals(50, inventoryItem.getStockQuantity());
    }

    @Test
    void getRestockLevel() {
        assertEquals(20, inventoryItem.getRestockLevel());
    }

    @Test
    void getLowStockLevel() {
        assertEquals(20, inventoryItem.getLowStockLevel());
    }

    @Test
    void getSupplierId() {
        assertEquals(101, inventoryItem.getSupplierId());
    }

    @Test
    void getStockMovementHistory() {
        assertNotNull(inventoryItem.getStockMovementHistory());
        assertEquals(1, inventoryItem.getStockMovementHistory().size());
    }

    @Test
    void addStock() {
        boolean result = inventoryItem.addStock(10, "ISS resupply");
        assertTrue(result);
        assertEquals(60, inventoryItem.getItemQuantity());
    }

    @Test
    void removeStock() {
        boolean result = inventoryItem.removeStock(10, "Orion assembly");
        assertTrue(result);
        assertEquals(40, inventoryItem.getItemQuantity());
    }

    @Test
    void isLowStock() {
        assertFalse(inventoryItem.isLowStock());
        inventoryItem.removeStock(35, "test");
        assertTrue(inventoryItem.isLowStock());
    }
}