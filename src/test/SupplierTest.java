package test;

import model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SupplierTest {

	private Supplier supplier;

	@BeforeEach
	void setUp() {
		supplier = new Supplier(1, "NASA Railworks", "orion@nasa.test", "Artemis Systems");
	}

	@Test
	void makeSupplier() {
		assertEquals(1, supplier.getId());
		assertEquals("NASA Railworks", supplier.getName());
		assertEquals("orion@nasa.test", supplier.getContactInfo());
		assertEquals("Artemis Systems", supplier.getItemType());
		assertTrue(supplier.getOrderHistory().isEmpty());
	}

	@Test
	void changeSupplier() {
		supplier.setName("Boeing Orbital Freight");
		supplier.setContactInfo("apollo@runway.test");
		supplier.setItemType("Hubble Payloads");

		assertEquals("Boeing Orbital Freight", supplier.getName());
		assertEquals("apollo@runway.test", supplier.getContactInfo());
		assertEquals("Hubble Payloads", supplier.getItemType());
	}

	@Test
	void saveHistory() {
		supplier.addOrderHistory("Artemis Rail-01 created");

		assertEquals(1, supplier.getOrderHistory().size());
		assertEquals("Artemis Rail-01 created", supplier.getOrderHistory().get(0));
	}

}