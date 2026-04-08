package service;
// This is used for the order service, which is the base class for both purchase and sales order services.

import model.Order;
import model.OrderStatus;
import model.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public abstract class OrderService {
    protected final List<Order> orders;
    protected final SupplierService supplierService;
    protected final InventoryService inventoryService;
    protected final FinanceService financeService;
    private int orderIdCounter;

    public OrderService(List<Order> orders, int orderIdCounter, SupplierService supplierService, InventoryService inventoryService) {
        this(orders, orderIdCounter, supplierService, inventoryService, new FinanceService());
    }

    public OrderService(List<Order> orders, int orderIdCounter, SupplierService supplierService, InventoryService inventoryService, FinanceService financeService) {
        this.orders = orders;
        this.orderIdCounter = orderIdCounter;
        this.supplierService = supplierService;
        this.inventoryService = inventoryService;
        this.financeService = financeService;
    }

    protected int nextOrderId() {
        return orderIdCounter++;
    }

    protected abstract boolean createOrder(Order order);

    protected boolean recordOrderTransaction(Order order, TransactionType transactionType) {
        BigDecimal totalAmount = BigDecimal.valueOf(order.calculateTotal());

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }

        return financeService.recordTransaction(order.getOrderId(), totalAmount, transactionType) != null;
    }

    protected boolean reverseOrderTransaction(Order order) {
        BigDecimal totalAmount = BigDecimal.valueOf(order.calculateTotal());

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }

        return financeService.reverseOrderTransaction(order.getOrderId()) != null;
    }

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
