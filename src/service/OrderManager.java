package service;
// This is used for managing both customer and purchase orders, providing a unified interface for order operations.

import model.CustomerOrder;
import model.Order;
import model.OrderStatus;
import model.PurchaseOrder;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final List<Order> allOrders;
    private final CustomerOrderService customerOrderService;
    private final PurchaseOrderService purchaseOrderService;
    private int nextOrderId;

    public OrderManager(SupplierService supplierService, InventoryService inventoryService) {
        this(supplierService, inventoryService, new FinanceService());
    }

    public OrderManager(SupplierService supplierService, InventoryService inventoryService, FinanceService financeService) {
        this.allOrders = new ArrayList<>();
        this.nextOrderId = 1;
        this.customerOrderService = new CustomerOrderService(allOrders, 1, supplierService, inventoryService, financeService);
        this.purchaseOrderService = new PurchaseOrderService(allOrders, 1, supplierService, inventoryService, financeService);
    }

    public int addCustomerOrder(int itemId, int quantity, double itemPrice) {
        return customerOrderService.addCustomerOrder(nextOrderId++, itemId, quantity, itemPrice);
    }

    public int addPurchaseOrder(int itemId, int quantity, double itemPrice, int supplierId) {
        return purchaseOrderService.addPurchaseOrder(nextOrderId++, itemId, quantity, itemPrice, supplierId);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(allOrders);
    }

    public boolean changeOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);

        if (order == null) {
            return false;
        }

        if (order instanceof CustomerOrder) {
            return customerOrderService.changeOrderStatus(orderId, newStatus);
        }

        if (order instanceof PurchaseOrder) {
            return purchaseOrderService.changeOrderStatus(orderId, newStatus);
        }

        return false;
    }

    public boolean cancelOrder(int orderId) {
        Order order = getOrderById(orderId);

        if (order == null) {
            return false;
        }

        if (order instanceof CustomerOrder) {
            return customerOrderService.cancelOrder(orderId);
        }

        if (order instanceof PurchaseOrder) {
            return purchaseOrderService.cancelOrder(orderId);
        }

        return false;
    }

    public boolean hasActiveOrdersForItem(int itemId) {
        if (itemId <= 0) {
            return false;
        }

        for (Order order : allOrders) {
            if (order.getItemId() == itemId && order.getStatus() != OrderStatus.CANCELLED) {
                return true;
            }
        }

        return false;
    }

    private Order getOrderById(int orderId) {
        for (Order order : allOrders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }
}


