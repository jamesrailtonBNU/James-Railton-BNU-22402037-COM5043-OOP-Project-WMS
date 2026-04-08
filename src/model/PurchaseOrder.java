package model;
// This is used for both purchase orders and sales orders, as they share common attributes and behaviors.

public class PurchaseOrder extends Order {

    private final int supplierId;

    public PurchaseOrder(int orderId, int itemId, String itemName, int quantity, double itemPrice, int supplierId) {
        super(orderId, itemId, itemName, quantity, itemPrice);
        this.supplierId = supplierId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    @Override
    public String getSupplierDisplay() {
        return String.valueOf(supplierId);
    }


    @Override
    public String getOrderType() {
        return "PurchaseOrder";
    }
}
