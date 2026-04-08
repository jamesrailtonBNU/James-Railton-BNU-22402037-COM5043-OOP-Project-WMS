package model;

public class CustomerOrder extends Order {
 // CustomerOrder class extends order class via inheritance due to common attributes and behaviors

    public CustomerOrder(int orderId, int itemId, String itemName, int quantity, double itemPrice) {
        super(orderId, itemId, itemName, quantity, itemPrice);
    }

    @Override
    public String getOrderType() {
        return "CustomerOrder";
    }
}
