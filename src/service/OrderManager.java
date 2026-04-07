package service;

import model.CustomerOrder;
import model.Order;
import model.OrderStatus;
import model.PurchaseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderManager {
    private final List<Order> allOrders;
    private final AtomicInteger orderIdCounter;
    private final CustomerOrderService customerOrderService;
    private final PurchaseOrderService purchaseOrderService;

    public OrderManager(SupplierService supplierService, InventoryService inventoryService) {
        this.allOrders = new ArrayList<>();
        this.orderIdCounter = new AtomicInteger(1);
        this.customerOrderService = new CustomerOrderService(allOrders, orderIdCounter, supplierService, inventoryService);
        this.purchaseOrderService = new PurchaseOrderService(allOrders, orderIdCounter, supplierService, inventoryService);
    }

    public int addCustomerOrder(int itemId, int quantity, double itemPrice) {
        return customerOrderService.addCustomerOrder(itemId, quantity, itemPrice);
    }

    public int addPurchaseOrder(int itemId, int quantity, double itemPrice, int supplierId) {
        return purchaseOrderService.addPurchaseOrder(itemId, quantity, itemPrice, supplierId);
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

    private Order getOrderById(int orderId) {
        for (Order order : allOrders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }
}


