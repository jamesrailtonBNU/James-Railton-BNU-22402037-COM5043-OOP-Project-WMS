package model;

public class InventoryItem {
    private final int itemId;
    private final String itemName;
    private final ItemCategory itemCategory;
    private int itemQuantity;
    private final int restockLevel;
    private final int supplierId;

    public InventoryItem(int itemId, String itemName, ItemCategory itemCategory, int itemQuantity, int restockLevel, int supplierId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.restockLevel = restockLevel;
        this.supplierId = supplierId; // Used to state if item was supplied by a supplier or not
    }
}


