package test;

import model.PurchaseOrder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderTest {

    private final PurchaseOrder order = new PurchaseOrder(1, 20, "Ariane Panel", 4, 99.99, 7);

    @Test
    void supplierId() {
        assertEquals(7, order.getSupplierId());
    }

    @Test
    void supplierDisplay() {
        assertEquals("7", order.getSupplierDisplay());
    }

    @Test
    void orderType() {
        assertEquals("PurchaseOrder", order.getOrderType());
    }
}