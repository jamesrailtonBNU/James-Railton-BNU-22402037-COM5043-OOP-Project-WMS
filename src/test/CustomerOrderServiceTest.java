package test;

import model.CustomerOrder;
import model.ItemCategory;
import model.Order;
import model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CustomerOrderService;
import service.InventoryService;
import service.SupplierService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderServiceTest {

    private List<Order> orders;
    private InventoryService inventoryService;
    private ExposedCustomerOrderService customerOrderService;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
        inventoryService = new InventoryService();
        inventoryService.addItem("Orion Tape", ItemCategory.CONSUMABLES, 10, 2, 1);
        customerOrderService = new ExposedCustomerOrderService(
                orders,
                new AtomicInteger(1),
                new SupplierService(),
                inventoryService);
    }

    @Test
    void addOrder() {
        int orderId = customerOrderService.addCustomerOrder(1, 3, 4.5);

        assertEquals(1, orderId);
        assertEquals(1, orders.size());
        assertTrue(orders.get(0) instanceof CustomerOrder);
        assertEquals(7, inventoryService.getItemById(1).getItemQuantity());
        assertTrue(orders.get(0).isInventoryUpdated());
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

        ExposedCustomerOrderService(List<Order> orders, AtomicInteger orderIdCounter, SupplierService supplierService, InventoryService inventoryService) {
            super(orders, orderIdCounter, supplierService, inventoryService);
        }

        boolean createOrderPublic(Order order) {
            return createOrder(order);
        }

        boolean cancelSpecificOrderPublic(Order order) {
            return cancelSpecificOrder(order);
        }
    }
}