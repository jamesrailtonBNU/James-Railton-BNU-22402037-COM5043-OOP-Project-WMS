package service;

import model.Order;
import model.OrderStatus;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class OrderService {
    protected final List<Order> orders;
    protected final SupplierService supplierService;
    protected final InventoryService inventoryService;
    private final AtomicInteger orderIdCounter;

    public OrderService(List<Order> orders, AtomicInteger orderIdCounter, SupplierService supplierService, InventoryService inventoryService) {
        this.orders = orders;
        this.orderIdCounter = orderIdCounter;
        this.supplierService = supplierService;
        this.inventoryService = inventoryService;
    }

    protected int nextOrderId() {
        return orderIdCounter.getAndIncrement();
    }

    protected abstract boolean createOrder(Order order);

    protected boolean beforeStatusChange(Order order, OrderStatus newStatus) {
        return true;
    }

    protected boolean afterStatusChange(Order order, OrderStatus newStatus) {
        return true;
    }

    protected abstract boolean cancelSpecificOrder(Order order);

    protected Order getOrderById(int orderId, Class<? extends Order> orderType) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId && orderType.isInstance(order)) {
                return order;
            }
        }
        return null;
    }

    protected boolean changeOrderStatus(int orderId, OrderStatus newStatus, Class<? extends Order> orderType) {
        Order order = getOrderById(orderId, orderType);

        if (order == null || newStatus == null || order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        if (!beforeStatusChange(order, newStatus)) {
            return false;
        }

        if (!order.changeStatus(newStatus)) {
            return false;
        }

        return afterStatusChange(order, newStatus);
    }

    protected boolean cancelOrder(int orderId, Class<? extends Order> orderType) {
        Order order = getOrderById(orderId, orderType);

        if (order == null) {
            return false;
        }

        return cancelSpecificOrder(order);
    }
}
