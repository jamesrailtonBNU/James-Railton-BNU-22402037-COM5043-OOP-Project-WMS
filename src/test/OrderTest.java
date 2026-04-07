package test;

import model.Order;
import model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private OrbitOrder order;

    @BeforeEach
    void setUp() {
        order = new OrbitOrder(1, 100, "Embraer Rivet Kit", 5, 2.5);
    }

    @Test
    void getOrderId() {
        assertEquals(1, order.getOrderId());
    }

    @Test
    void getItemName() {
        assertEquals("Embraer Rivet Kit", order.getItemName());
    }

    @Test
    void getItemId() {
        assertEquals(100, order.getItemId());
    }

    @Test
    void getQuantity() {
        assertEquals(5, order.getQuantity());
    }

    @Test
    void getItemPrice() {
        assertEquals(2.5, order.getItemPrice());
    }

    @Test
    void getStatus() {
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void setStatus() {
        order.updateStatus(OrderStatus.IN_TRANSIT);
        assertEquals(OrderStatus.IN_TRANSIT, order.getStatus());
    }

    @Test
    void isInventoryUpdated() {
        assertFalse(order.isInventoryUpdated());
    }

    @Test
    void setInventoryUpdated() {
        order.setInventoryUpdated(true);
        assertTrue(order.isInventoryUpdated());
    }

    @Test
    void calculateTotal() {
        assertEquals(12.5, order.calculateTotal());
    }

    @Test
    void cancelOrder() {
        assertTrue(order.cancelOrder());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(0, order.getQuantity());
        assertFalse(order.cancelOrder());
    }

    @Test
    void changeStatus() {
        assertTrue(order.changeStatus(OrderStatus.PROCESSED));
        assertEquals(OrderStatus.PROCESSED, order.getStatus());
        assertFalse(order.changeStatus(null));

        order.cancelOrder();
        assertFalse(order.changeStatus(OrderStatus.DELIVERED));
    }

    @Test
    void getSupplierDisplay() {
        assertEquals("-", order.getSupplierDisplay());
    }

    @Test
    void handleSupplierId() {
        assertEquals("-", order.handleSupplierId());
    }

    @Test
    void getOrderType() {
        assertEquals("OrbitOrder", order.getOrderType());
    }

    private static class OrbitOrder extends Order {

        OrbitOrder(int orderId, int itemId, String itemName, int quantity, double itemPrice) {
            super(orderId, itemId, itemName, quantity, itemPrice);
        }

        void updateStatus(OrderStatus status) {
            setStatus(status);
        }

        @Override
        public String getOrderType() {
            return "OrbitOrder";
        }
    }
}