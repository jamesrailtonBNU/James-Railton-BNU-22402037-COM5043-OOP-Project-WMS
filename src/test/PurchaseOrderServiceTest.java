package test;

import model.ItemCategory;
import model.Order;
import model.OrderStatus;
import model.PurchaseOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FinanceService;
import service.InventoryService;
import service.PurchaseOrderService;
import service.SupplierService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderServiceTest {

    private List<Order> orders;
    private SupplierService supplierService;
    private InventoryService inventoryService;
    private FinanceService financeService;
    private ExposedPurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
        supplierService = new SupplierService();
        supplierService.addSupplier("Arianespace Hangar Tools", "artemis2@nasa.test", "JWST Payloads");
        inventoryService = new InventoryService();
        financeService = new FinanceService();
        inventoryService.addItem("Perseverance Drill", ItemCategory.POWER_TOOLS, 5, 1, 1);
        purchaseOrderService = new ExposedPurchaseOrderService(
                orders,
                1,
                supplierService,
                inventoryService,
                financeService);
    }

    @Test
    void addPurchase() {
        int orderId = purchaseOrderService.addPurchaseOrder(1, 4, 99.99, 1);

        assertEquals(1, orderId);
        assertEquals(1, orders.size());
        assertTrue(orders.get(0) instanceof PurchaseOrder);
        assertEquals(1, supplierService.getSupplierOrderHistory(1).size());
        assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.valueOf(-399.96)));
    }

    @Test
    void changePurchase() {
        int orderId = purchaseOrderService.addPurchaseOrder(1, 3, 99.99, 1);

        assertTrue(purchaseOrderService.changeOrderStatus(orderId, OrderStatus.DELIVERED));
        assertEquals(OrderStatus.DELIVERED, orders.get(0).getStatus());
        assertEquals(8, inventoryService.getItemById(1).getItemQuantity());
        assertTrue(orders.get(0).isInventoryUpdated());
    }

    @Test
    void cancelPurchase() {
        int orderId = purchaseOrderService.addPurchaseOrder(1, 3, 99.99, 1);

        assertTrue(purchaseOrderService.cancelOrder(orderId));
        assertEquals(OrderStatus.CANCELLED, orders.get(0).getStatus());
        assertEquals(0, orders.get(0).getQuantity());
        assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void makePurchase() {
        PurchaseOrder order = new PurchaseOrder(10, 1, "Perseverance Drill", 2, 99.99, 1);

        assertTrue(purchaseOrderService.createOrderPublic(order));
        assertEquals(1, supplierService.getSupplierOrderHistory(1).size());
        assertTrue(supplierService.getSupplierOrderHistory(1).get(0).contains("Purchase Order ID 10 created"));
    }

    @Test
    void beforeDelivery() {
        PurchaseOrder order = new PurchaseOrder(20, 1, "Perseverance Drill", 2, 99.99, 1);

        assertTrue(purchaseOrderService.beforeStatusChangePublic(order, OrderStatus.DELIVERED));
        assertEquals(7, inventoryService.getItemById(1).getItemQuantity());
        assertTrue(order.isInventoryUpdated());
    }

    @Test
    void afterChange() {
        PurchaseOrder order = new PurchaseOrder(30, 1, "Perseverance Drill", 2, 99.99, 1);
        order.changeStatus(OrderStatus.PROCESSED);

        assertTrue(purchaseOrderService.afterStatusChangePublic(order, OrderStatus.PROCESSED));
        assertEquals(1, supplierService.getSupplierOrderHistory(1).size());
        assertTrue(supplierService.getSupplierOrderHistory(1).get(0).contains("status changed to [PROCESSED]"));
    }

    @Test
    void stopDeliveredCancel() {
        PurchaseOrder order = new PurchaseOrder(40, 1, "Perseverance Drill", 2, 99.99, 1);
        order.setInventoryUpdated(true);

        assertFalse(purchaseOrderService.cancelSpecificOrderPublic(order));
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    private static class ExposedPurchaseOrderService extends PurchaseOrderService {

        ExposedPurchaseOrderService(List<Order> orders, int nextOrderId, SupplierService supplierService, InventoryService inventoryService, FinanceService financeService) {
            super(orders, nextOrderId, supplierService, inventoryService, financeService);
        }

        boolean createOrderPublic(Order order) {
            return createOrder(order);
        }

        boolean beforeStatusChangePublic(Order order, OrderStatus newStatus) {
            return beforeStatusChange(order, newStatus);
        }

        boolean afterStatusChangePublic(Order order, OrderStatus newStatus) {
            return afterStatusChange(order, newStatus);
        }

        boolean cancelSpecificOrderPublic(Order order) {
            return cancelSpecificOrder(order);
        }
    }
}