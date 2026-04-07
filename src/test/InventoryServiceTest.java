package test;

import model.InventoryItem;
import model.ItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InventoryService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private InventoryService inventoryService;

    @BeforeEach
    void TestSetUp() {
        inventoryService = new InventoryService();
    }

    @Test
    void addItem() {
        InventoryItem item = inventoryService.addItem("Mars Torque Key", ItemCategory.HAND_TOOLS, 10, 3, 100);
        assertNotNull(item);
        assertEquals("Mars Torque Key", item.getItemName());
        assertEquals(1, item.getItemId());
        assertEquals(1, inventoryService.getAllItems().size());
    }

    @Test
    void addStock() {
        InventoryItem item = inventoryService.addItem("ESA Flight Gloves", ItemCategory.PPE, 5, 2, 200);
        boolean result = inventoryService.addStock(item.getItemId(), 4, "JWST refill");
        assertTrue(result);
        assertEquals(9, item.getItemQuantity());
        assertFalse(inventoryService.addStock(999, 1, "Invalid id"));
    }

    @Test
    void removeStock() {
        InventoryItem item = inventoryService.addItem("Orion Seal Tape", ItemCategory.CONSUMABLES, 8, 2, 300);
        boolean result = inventoryService.removeStock(item.getItemId(), 3, "ISS repair");
        assertTrue(result);
        assertEquals(5, item.getItemQuantity());
        assertFalse(inventoryService.removeStock(item.getItemId(), 10, "Too much"));
    }

    @Test
    void getAllItems() {
        inventoryService.addItem("Ariane Rivet Drill", ItemCategory.POWER_TOOLS, 2, 1, 400);
        List<InventoryItem> allItems = inventoryService.getAllItems();
        allItems.clear();
        assertEquals(1, inventoryService.getAllItems().size());
    }

    @Test
    void getLowStockItems() {
        InventoryItem low = inventoryService.addItem("Blue Origin EVA Mask", ItemCategory.PPE, 2, 2, 500);
        inventoryService.addItem("Eurostar Track Wrench", ItemCategory.HAND_TOOLS, 10, 2, 600);
        List<InventoryItem> lowStockItems = inventoryService.getLowStockItems();
        assertEquals(1, lowStockItems.size());
        assertEquals(low.getItemId(), lowStockItems.getFirst().getItemId());
    }

    @Test
    void getItemById() {
        InventoryItem item = inventoryService.addItem("Delta Cargo Crate", ItemCategory.PACKAGING, 15, 5, 700);
        assertEquals(item, inventoryService.getItemById(item.getItemId()));
        assertNull(inventoryService.getItemById(0));
        assertNull(inventoryService.getItemById(999));
    }
}