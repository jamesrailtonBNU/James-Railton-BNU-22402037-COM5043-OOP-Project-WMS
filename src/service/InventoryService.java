package service;

import model.InventoryItem;
import model.ItemCategory;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {
    private final List<InventoryItem> stockItems;
    private int itemIdCounter = 1;

    public InventoryService() {
        stockItems = new ArrayList<>();
    }

    public InventoryItem addItem(String itemName, ItemCategory itemCategory, int initialQuantity, int lowStockLevel, int supplierId) {
        if (itemName == null || itemName.trim().isEmpty() || itemCategory == null || initialQuantity < 0 || lowStockLevel < 0 || supplierId < 0) {
            return null;
        }

        int itemId = generateItemId();
        InventoryItem item = new InventoryItem(itemId, itemName, itemCategory, initialQuantity, lowStockLevel, supplierId);
        stockItems.add(item);
        return item;
    }

    public boolean addStock(int itemId, int quantity, String note) {
        InventoryItem item = getItemById(itemId);
        return item != null && item.addStock(quantity, note);
    }

    public boolean removeStock(int itemId, int quantity, String note) {
        InventoryItem item = getItemById(itemId);
        return item != null && item.removeStock(quantity, note);
    }

    public List<InventoryItem> getAllItems() {
        return new ArrayList<>(stockItems);
    }

    public List<InventoryItem> getLowStockItems() {
        List<InventoryItem> lowStockItems = new ArrayList<>();

        for (InventoryItem item : stockItems) {
            if (item.isLowStock()) {
                lowStockItems.add(item);
            }
        }

        return lowStockItems;
    }

    public InventoryItem getItemById(int itemId) {
        if (itemId <= 0) {
            return null;
        }

        for (InventoryItem item : stockItems) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }

        return null;
    }

    private int generateItemId() {
        return itemIdCounter++;
    }
}

