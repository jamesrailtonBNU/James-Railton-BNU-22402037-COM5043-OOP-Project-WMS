package test;

import model.CustomerOrder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderTest {

    @Test
    void orderType() {
        CustomerOrder order = new CustomerOrder(1, 10, "Orion Tape", 3, 4.5);

        assertEquals("CustomerOrder", order.getOrderType());
    }
}