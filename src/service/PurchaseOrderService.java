package service;
// this is used for managing purchase orders, including creating new purchase orders, changing their status, and cancelling them.


import model.Order;
import model.OrderStatus;
import model.PurchaseOrder;
import model.InventoryItem;
import model.TransactionType;
import java.util.List;

public class PurchaseOrderService extends OrderService {

    public PurchaseOrderService(List<Order> orders, int nextOrderId, SupplierService supplierService, InventoryService inventoryService) {
        this(orders, nextOrderId, supplierService, inventoryService, new FinanceService());
    }

    public PurchaseOrderService(List<Order> orders, int nextOrderId, SupplierService supplierService, InventoryService inventoryService, FinanceService financeService) {
        super(orders, nextOrderId, supplierService, inventoryService, financeService);
    }

    public int addPurchaseOrder(int itemId, int quantity, double itemPrice, int supplierId) {
        return addPurchaseOrder(nextOrderId(), itemId, quantity, itemPrice, supplierId);
    }

    int addPurchaseOrder(int orderId, int itemId, int quantity, double itemPrice, int supplierId) {
        InventoryItem item = inventoryService.getItemById(itemId);

        if (item == null || quantity <= 0 || itemPrice < 0 || supplierId <= 0 || !supplierService.supplierExists(supplierId)) {
            return -1;
        }

        if (item.getSupplierId() > 0 && item.getSupplierId() != supplierId) {
            return -1;
        }

        PurchaseOrder order = new PurchaseOrder(orderId, item.getItemId(), item.getItemName(), quantity, itemPrice, supplierId);

        if (!createOrder(order)) {
            return -1;
        }

        if (!recordOrderTransaction(order, TransactionType.EXPENSE)) {
            return -1;
        }

        orders.add(order);
        return order.getOrderId();
    }

    public boolean changeOrderStatus(int orderId, OrderStatus newStatus) {
        return super.changeOrderStatus(orderId, newStatus, PurchaseOrder.class);
    }

    public boolean cancelOrder(int orderId) {
        return super.cancelOrder(orderId, PurchaseOrder.class);
    }

    @Override
    protected boolean createOrder(Order order) {
        PurchaseOrder purchaseOrder = (PurchaseOrder) order;
        supplierService.addOrderHistory(
                purchaseOrder.getSupplierId(),
                "Purchase Order ID " + order.getOrderId() + " created for "
                        + order.getItemId() + " - " + order.getItemName()
                        + " x" + order.getQuantity() + " [" + order.getStatus() + "]");
        return true;
    }

    @Override
    protected boolean beforeStatusChange(Order order, OrderStatus newStatus) {
        if (newStatus == OrderStatus.DELIVERED && !order.isInventoryUpdated()) {
            boolean received = inventoryService.addStock(
                    order.getItemId(),
                    order.getQuantity(),
                    "Purchase order ID " + order.getOrderId() + " delivered");

            if (!received) {
                return false;
            }

            order.setInventoryUpdated(true);
        }

        return true;
    }

    @Override
    protected boolean afterStatusChange(Order order, OrderStatus newStatus) {
        PurchaseOrder purchaseOrder = (PurchaseOrder) order;
        supplierService.addOrderHistory(
                purchaseOrder.getSupplierId(),
                "Purchase Order ID " + order.getOrderId() + " status changed to [" + order.getStatus() + "]");
        return true;
    }

    @Override
    protected boolean cancelSpecificOrder(Order order) {
        PurchaseOrder purchaseOrder = (PurchaseOrder) order;

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        if (order.isInventoryUpdated()) {
            return false;
        }

        if (!reverseOrderTransaction(order)) {
            return false;
        }

        boolean cancelled = order.cancelOrder();

        if (cancelled) {
            supplierService.addOrderHistory(
                    purchaseOrder.getSupplierId(),
                    "Purchase Order ID " + order.getOrderId() + " cancelled and quantity set to 0");
        }

        return cancelled;
    }
}

