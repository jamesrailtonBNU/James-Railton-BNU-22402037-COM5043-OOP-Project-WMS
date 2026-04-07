package test;

import model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SupplierService;

import static org.junit.jupiter.api.Assertions.*;

class SupplierServiceTest {

    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        supplierService = new SupplierService();
    }

    @Test
    void addSupplier() {
        supplierService.addSupplier("NASA Cargo", "apollo11@nasa.test", "JWST Systems");

        Supplier supplier = supplierService.getSupplierById(1);
        assertNotNull(supplier);
        assertEquals("NASA Cargo", supplier.getName());
        assertEquals("apollo11@nasa.test", supplier.getContactInfo());
        assertEquals("JWST Systems", supplier.getItemType());
    }

    @Test
    void changeSupplier() {
        supplierService.addSupplier("Hitachi Rail Works", "signal@rail.test", "Eurostar Components");

        assertTrue(supplierService.updateSupplier(1, "Airbus Orbital Supply", "apollo@sky.test", "Ariane Payloads"));

        Supplier supplier = supplierService.getSupplierById(1);
        assertNotNull(supplier);
        assertEquals("Airbus Orbital Supply", supplier.getName());
        assertEquals("apollo@sky.test", supplier.getContactInfo());
        assertEquals("Ariane Payloads", supplier.getItemType());
        assertFalse(supplierService.updateSupplier(99, "X", "Y", "Z"));
    }

    @Test
    void removeSupplier() {
        supplierService.addSupplier("Emirates Hangar Works", "tower@plane.test", "Boeing Spares");

        assertTrue(supplierService.deleteSupplier(1));
        assertNull(supplierService.getSupplierById(1));
        assertFalse(supplierService.deleteSupplier(1));
    }

    @Test
    void findSupplier() {
        supplierService.addSupplier("Network Rail Works", "signal@track.test", "LNER Signals");

        assertNotNull(supplierService.getSupplierById(1));
        assertNull(supplierService.getSupplierById(999));
    }

    @Test
    void hasSupplier() {
        supplierService.addSupplier("Blue Origin Freight", "cargo@nasa.test", "ISS Cargo");

        assertTrue(supplierService.supplierExists(1));
        assertFalse(supplierService.supplierExists(2));
    }

    @Test
    void listSuppliers() {
        supplierService.addSupplier("ESA Station Supply", "dock@orbit.test", "Galileo Arrays");

        var suppliers = supplierService.getAllSuppliers();
        assertEquals(1, suppliers.size());

        suppliers.clear();
        assertEquals(1, supplierService.getAllSuppliers().size());
    }

    @Test
    void addHistory() {
        supplierService.addSupplier("Qatar Airways Parts Hub", "jet@plane.test", "Airbus Panels");

        supplierService.addOrderHistory(1, "Apollo Flight Order 1 created");

        assertEquals(1, supplierService.getSupplierOrderHistory(1).size());
        assertEquals("Apollo Flight Order 1 created", supplierService.getSupplierOrderHistory(1).get(0));
    }

    @Test
    void readHistory() {
        supplierService.addSupplier("Siemens Mobility Supply", "mission@rail.test", "Avanti Modules");
        supplierService.addOrderHistory(1, "Orion Rail Order 1 created");

        var history = supplierService.getSupplierOrderHistory(1);
        assertNotNull(history);
        assertEquals(1, history.size());

        history.clear();
        assertEquals(1, supplierService.getSupplierOrderHistory(1).size());
        assertNull(supplierService.getSupplierOrderHistory(999));
    }
}