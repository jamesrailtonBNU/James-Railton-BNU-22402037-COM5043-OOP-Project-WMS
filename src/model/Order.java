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
    }

}
