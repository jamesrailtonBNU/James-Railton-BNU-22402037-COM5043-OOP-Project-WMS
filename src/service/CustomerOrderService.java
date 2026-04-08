package service;
// This is used for the SupplierOrderService and CustomerOrderService to avoid code duplication. It is not intended to be used directly by external code.


import model.CustomerOrder;
import model.Order;
import model.OrderStatus;
import model.InventoryItem;
import model.TransactionType;
import java.util.List;

public class CustomerOrderService extends OrderService {

    public CustomerOrderService(List<Order> orders, int nextOrderId, SupplierService supplierService, InventoryService inventoryService) {
        this(orders, nextOrderId, supplierService, inventoryService, new FinanceService());
    }

    public CustomerOrderService(List<Order> orders, int nextOrderId, SupplierService supplierService, InventoryService inventoryService, FinanceService financeService) {
        super(orders, nextOrderId, supplierService, inventoryService, financeService);
    }

    public int addCustomerOrder(int itemId, int quantity, double itemPrice) {
        return addCustomerOrder(nextOrderId(), itemId, quantity, itemPrice);
    }

    int addCustomerOrder(int orderId, int itemId, int quantity, double itemPrice) {
        InventoryItem item = inventoryService.getItemById(itemId);

        if (item == null || quantity <= 0 || itemPrice < 0 || item.getStockQuantity() < quantity) {
            return -1;
        }

        CustomerOrder order = new CustomerOrder(orderId, item.getItemId(), item.getItemName(), quantity, itemPrice);

        if (!createOrder(order)) {
            return -1;
        }

        if (!recordOrderTransaction(order, TransactionType.INCOME)) {
            if (order.isInventoryUpdated()) {
                inventoryService.addStock(
                        order.getItemId(),
                        order.getQuantity(),
                        "Customer order ID " + order.getOrderId() + " finance rollback");
                order.setInventoryUpdated(false);
            }
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
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        int originalQuantity = order.getQuantity();

        if (order.isInventoryUpdated()) {
            boolean restocked = inventoryService.addStock(
                    order.getItemId(),
                    originalQuantity,
                    "Customer order ID " + order.getOrderId() + " cancelled");

            if (!restocked) {
                return false;
            }
        }

        if (!reverseOrderTransaction(order)) {
            if (order.isInventoryUpdated()) {
                inventoryService.removeStock(
                        order.getItemId(),
                        originalQuantity,
                        "Customer order ID " + order.getOrderId() + " cancellation rollback");
            }
            return false;
        }

        order.setInventoryUpdated(false);

        return order.cancelOrder();
    }
}

