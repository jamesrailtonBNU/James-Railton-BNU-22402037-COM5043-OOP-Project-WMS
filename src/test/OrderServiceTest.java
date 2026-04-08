package test;

import model.Order;
import model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InventoryService;
import service.OrderService;
import service.SupplierService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private List<Order> orders;
    private OrbitService orderService;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
        orderService = new OrbitService(orders, 1, new SupplierService(), new InventoryService());
    }

    @Test
    void nextId() {
        assertEquals(1, orderService.nextOrderIdPublic());
        assertEquals(2, orderService.nextOrderIdPublic());
    }

    @Test
    void makeOrder() {
        OrbitOrder order = new OrbitOrder(1, 100, "Voyager Bolt Kit", 5, 2.5);

        assertTrue(orderService.createOrderPublic(order));
        assertEquals(1, orders.size());
        assertSame(order, orders.get(0));
    }

    @Test
    void beforeChange() {
        assertTrue(orderService.beforeStatusChangePublic(new OrbitOrder(1, 100, "Voyager Bolt Kit", 5, 2.5), OrderStatus.PROCESSED));
    }

    @Test
    void afterChange() {
        assertTrue(orderService.afterStatusChangePublic(new OrbitOrder(1, 100, "Voyager Bolt Kit", 5, 2.5), OrderStatus.DELIVERED));
    }

    @Test
    void cancelOne() {
        OrbitOrder order = new OrbitOrder(1, 100, "Voyager Bolt Kit", 5, 2.5);
        assertTrue(orderService.cancelSpecificOrderPublic(order));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void findById() {
        OrbitOrder order = new OrbitOrder(1, 100, "Voyager Bolt Kit", 5, 2.5);
        RunwayOrder otherOrder = new RunwayOrder(2, 200, "Eurostar Brake Set", 2, 50.0);
        orders.add(order);
        orders.add(otherOrder);

        assertSame(order, orderService.getOrderByIdPublic(1, OrbitOrder.class));
        assertNull(orderService.getOrderByIdPublic(2, OrbitOrder.class));
        assertNull(orderService.getOrderByIdPublic(999, OrbitOrder.class));
    }

    @Test
    void changeStatus() {
        OrbitOrder order = new OrbitOrder(1, 100, "Cassini Tool Pack", 5, 2.5);
        orders.add(order);

        assertTrue(orderService.changeOrderStatusPublic(1, OrderStatus.DELIVERED, OrbitOrder.class));
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertTrue(orderService.beforeCalled);
        assertTrue(orderService.afterCalled);
    }

    @Test
    void cancelById() {
        OrbitOrder order = new OrbitOrder(1, 100, "Cassini Tool Pack", 5, 2.5);
        orders.add(order);

        assertTrue(orderService.cancelOrderPublic(1, OrbitOrder.class));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertFalse(orderService.cancelOrderPublic(999, OrbitOrder.class));
    }

    private static class OrbitService extends OrderService {

        private boolean beforeCalled;
        private boolean afterCalled;

        OrbitService(List<Order> orders, int nextOrderId, SupplierService supplierService, InventoryService inventoryService) {
            super(orders, nextOrderId, supplierService, inventoryService);
        }

        int nextOrderIdPublic() {
            return nextOrderId();
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

        Order getOrderByIdPublic(int orderId, Class<? extends Order> orderType) {
            return getOrderById(orderId, orderType);
        }

        boolean changeOrderStatusPublic(int orderId, OrderStatus newStatus, Class<? extends Order> orderType) {
            beforeCalled = false;
            afterCalled = false;
            return changeOrderStatus(orderId, newStatus, orderType);
        }

        boolean cancelOrderPublic(int orderId, Class<? extends Order> orderType) {
            return cancelOrder(orderId, orderType);
        }

        @Override
        protected boolean createOrder(Order order) {
            orders.add(order);
            return true;
        }

        @Override
        protected boolean beforeStatusChange(Order order, OrderStatus newStatus) {
            beforeCalled = true;
            return super.beforeStatusChange(order, newStatus);
        }

        @Override
        protected boolean afterStatusChange(Order order, OrderStatus newStatus) {
            afterCalled = true;
            return super.afterStatusChange(order, newStatus);
        }

        @Override
        protected boolean cancelSpecificOrder(Order order) {
            return order.cancelOrder();
        }
    }

    private static class OrbitOrder extends Order {

        OrbitOrder(int orderId, int itemId, String itemName, int quantity, double itemPrice) {
            super(orderId, itemId, itemName, quantity, itemPrice);
        }

        @Override
        public String getOrderType() {
            return "OrbitOrder";
        }
    }

    private static class RunwayOrder extends Order {

        RunwayOrder(int orderId, int itemId, String itemName, int quantity, double itemPrice) {
            super(orderId, itemId, itemName, quantity, itemPrice);
        }

        @Override
        public String getOrderType() {
            return "RunwayOrder";
        }
    }
}