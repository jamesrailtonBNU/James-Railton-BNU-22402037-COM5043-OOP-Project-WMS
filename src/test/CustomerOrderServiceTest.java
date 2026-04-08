package test;

import model.CustomerOrder;
import model.ItemCategory;
import model.Order;
import model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CustomerOrderService;
import service.FinanceService;
import service.InventoryService;
import service.SupplierService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderServiceTest {

    private List<Order> orders;
    private InventoryService inventoryService;
    private FinanceService financeService;
    private ExposedCustomerOrderService customerOrderService;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
        inventoryService = new InventoryService();
        financeService = new FinanceService();
        inventoryService.addItem("Orion Tape", ItemCategory.CONSUMABLES, 10, 2, 1);
        customerOrderService = new ExposedCustomerOrderService(
                orders,
                1,
                new SupplierService(),
                inventoryService,
                financeService);
    }

    @Test
    void addOrder() {
        int orderId = customerOrderService.addCustomerOrder(1, 3, 4.5);

        assertEquals(1, orderId);
        assertEquals(1, orders.size());
        assertTrue(orders.get(0) instanceof CustomerOrder);
        assertEquals(7, inventoryService.getItemById(1).getItemQuantity());
        assertTrue(orders.get(0).isInventoryUpdated());
        assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.valueOf(13.5)));
    }

    @Test
    void changeStatus() {
        int orderId = customerOrderService.addCustomerOrder(1, 2, 4.5);

        assertTrue(customerOrderService.changeOrderStatus(orderId, OrderStatus.PROCESSED));
        assertEquals(OrderStatus.PROCESSED, orders.get(0).getStatus());
    }

    @Test
    void cancelOrder() {
        int orderId = customerOrderService.addCustomerOrder(1, 2, 4.5);

        assertTrue(customerOrderService.cancelOrder(orderId));
        assertEquals(OrderStatus.CANCELLED, orders.get(0).getStatus());
        assertEquals(10, inventoryService.getItemById(1).getItemQuantity());
        assertFalse(orders.get(0).isInventoryUpdated());
        assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void makeOrder() {
        CustomerOrder order = new CustomerOrder(10, 1, "Orion Tape", 2, 4.5);

        assertTrue(customerOrderService.createOrderPublic(order));
        assertEquals(8, inventoryService.getItemById(1).getItemQuantity());
        assertTrue(order.isInventoryUpdated());
    }

    @Test
    void cancelOne() {
        CustomerOrder order = new CustomerOrder(20, 1, "Orion Tape", 2, 4.5);
        assertTrue(customerOrderService.createOrderPublic(order));

        assertTrue(customerOrderService.cancelSpecificOrderPublic(order));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(10, inventoryService.getItemById(1).getItemQuantity());
        assertFalse(order.isInventoryUpdated());
    }

    private static class ExposedCustomerOrderService extends CustomerOrderService {

        ExposedCustomerOrderService(List<Order> orders, int nextOrderId, SupplierService supplierService, InventoryService inventoryService, FinanceService financeService) {
            super(orders, nextOrderId, supplierService, inventoryService, financeService);
        }

        boolean createOrderPublic(Order order) {
            return createOrder(order);
        }

        boolean cancelSpecificOrderPublic(Order order) {
            return cancelSpecificOrder(order);
        }
    }
}