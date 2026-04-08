package model;
// This is used for the InventoryItem class to represent an item in the inventory, including its details and stock movement history.


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryItem {
    private final int itemId;
    private final String itemName;
    private final ItemCategory itemCategory;
    private int itemQuantity;
    private final int restockLevel;
    private final int supplierId;
    private final List<ItemMovementRecord> stockMovementHistory;

    public InventoryItem(int itemId, String itemName, ItemCategory itemCategory, int itemQuantity, int restockLevel, int supplierId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.restockLevel = restockLevel;
        this.supplierId = supplierId;
        this.stockMovementHistory = new ArrayList<>();

        // Record the initial stock quantity as an addition to the movement history
        if (itemQuantity > 0) {
            stockMovementHistory.add(new ItemMovementRecord(MovementType.ADD, itemQuantity, "Opening stock balance"));
        }
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public int getStockQuantity() {
        return itemQuantity;
    }

    public int getRestockLevel() {
        return restockLevel;
    }

    public int getLowStockLevel() {
        return restockLevel;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public List<ItemMovementRecord> getStockMovementHistory() {
        return Collections.unmodifiableList(stockMovementHistory);
    }

    public boolean addStock(int quantity, String note) {
        if (quantity <= 0) {
            return false;
        }
        itemQuantity += quantity;
        stockMovementHistory.add(new ItemMovementRecord(MovementType.ADD, quantity, note));
        return true;
    }

    public boolean removeStock(int quantity, String note) {
        if (quantity <= 0 || quantity > itemQuantity) {
            return false;
        }

        itemQuantity -= quantity;
        stockMovementHistory.add(new ItemMovementRecord(MovementType.REMOVE, quantity, note));
        return true;
    }

    public boolean isLowStock() {
        return itemQuantity <= restockLevel;
    }
}


