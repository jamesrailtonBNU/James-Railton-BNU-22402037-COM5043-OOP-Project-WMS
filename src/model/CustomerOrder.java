package model;

public class CustomerOrder extends Order {

    public CustomerOrder(int orderId, int itemId, String itemName, int quantity, double itemPrice) {
        super(orderId, itemId, itemName, quantity, itemPrice);
    }

    @Override
    public String getOrderType() {
        return "CustomerOrder";
    }
}
