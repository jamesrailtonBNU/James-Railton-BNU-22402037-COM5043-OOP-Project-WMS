package model;

public abstract class Order {
    private final int orderId;
    private final int itemId;
    private final String itemName;
    private int quantity;
    private final double itemPrice;
    private OrderStatus status;
    private boolean inventoryUpdated;

    public Order(int orderId, int itemId, String itemName, int quantity, double itemPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.status = OrderStatus.PENDING;
        this.inventoryUpdated = false;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    protected void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isInventoryUpdated() {
        return inventoryUpdated;
    }

    public void setInventoryUpdated(boolean inventoryUpdated) {
        this.inventoryUpdated = inventoryUpdated;
    }

    public double calculateTotal() {
        return quantity * itemPrice;
    }

    public boolean cancelOrder() {
        if (status == OrderStatus.CANCELLED) {
            return false;
        }

        status = OrderStatus.CANCELLED;
        quantity = 0;
        return true;
    }

    public boolean changeStatus(OrderStatus newStatus) {
        if (status == OrderStatus.CANCELLED || newStatus == null) {
            return false;
        }

        return switch (newStatus) {
            case PENDING, IN_TRANSIT, PROCESSED, DELIVERED, DELAYED -> {
                setStatus(newStatus);
                yield true;
            }
            default -> false;
        };
    }

    public String getSupplierDisplay() {
        return "-";
    }

    public String handleSupplierId() {
        return getSupplierDisplay();
    }

    public abstract String getOrderType();
}
