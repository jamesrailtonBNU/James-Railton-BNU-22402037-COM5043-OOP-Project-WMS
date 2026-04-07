package service;

import model.CustomerOrder;
import model.Order;
import model.OrderStatus;
import model.InventoryItem;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerOrderService extends OrderService {

    public CustomerOrderService(List<Order> orders, AtomicInteger orderIdCounter, SupplierService supplierService, InventoryService inventoryService) {
        super(orders, orderIdCounter, supplierService, inventoryService);
    }

    public int addCustomerOrder(int itemId, int quantity, double itemPrice) {
        InventoryItem item = inventoryService.getItemById(itemId);

        if (item == null || quantity <= 0 || itemPrice < 0 || item.getStockQuantity() < quantity) {
            return -1;
        }

        CustomerOrder order = new CustomerOrder(nextOrderId(), item.getItemId(), item.getItemName(), quantity, itemPrice);

        if (!createOrder(order)) {
            return -1;
        }

        orders.add(order);
        return order.getOrderId();
    }

    public boolean changeOrderStatus(int orderId, OrderStatus newStatus) {
        return super.changeOrderStatus(orderId, newStatus, CustomerOrder.class);
    }

    public boolean cancelOrder(int orderId) {
        return super.cancelOrder(orderId, CustomerOrder.class);
    }

    @Override
    protected boolean createOrder(Order order) {
        boolean updated = inventoryService.removeStock(
                order.getItemId(),
                order.getQuantity(),
                "Customer order ID " + order.getOrderId() + " created");

        if (!updated) {
            return false;
        }

        order.setInventoryUpdated(true);
        return true;
    }

    @Override
    protected boolean cancelSpecificOrder(Order order) {
        if (order.isInventoryUpdated()) {
            boolean restocked = inventoryService.addStock(
                    order.getItemId(),
                    order.getQuantity(),
                    "Customer order ID " + order.getOrderId() + " cancelled");

            if (!restocked) {
                return false;
            }

            order.setInventoryUpdated(false);
        }

        return order.cancelOrder();
    }
}

