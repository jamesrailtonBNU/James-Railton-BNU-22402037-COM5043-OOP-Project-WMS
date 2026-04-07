package service;

import model.Supplier;
import java.util.ArrayList;
import java.util.List;

public class SupplierService {
    private final List<Supplier> suppliers;
    private int idCounter = 1;

    public SupplierService() {
        suppliers = new ArrayList<>();
    }

    public void addSupplier(String name, String contactInfo, String itemType) {
        int id = idCounter++;
        Supplier supplier = new Supplier(id, name, contactInfo, itemType);
        suppliers.add(supplier);
    }

    public boolean updateSupplier(int id, String name, String contactInfo, String itemType) {
        Supplier supplier = getSupplierById(id);
        if (supplier != null) {
            supplier.setName(name);
            supplier.setContactInfo(contactInfo);
            supplier.setItemType(itemType);
            return true;
        }
        return false;
    }

    public boolean deleteSupplier(int id) {
        Supplier supplier = getSupplierById(id);
        if (supplier != null) {
            suppliers.remove(supplier);
            return true;
        }
        return false;
    }

    public Supplier getSupplierById(int id) {
        for (Supplier supplier : suppliers) {
            if (supplier.getId() == id) {
                return supplier;
            }
        }
        return null;
    }

    public boolean supplierExists(int id) {
        return getSupplierById(id) != null;
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers);
    }

    public void addOrderHistory(int supplierId, String historyEntry) {
        Supplier supplier = getSupplierById(supplierId);
        if (supplier != null) {
            supplier.addOrderHistory(historyEntry);
        }
    }

    public List<String> getSupplierOrderHistory(int supplierId) {
        Supplier supplier = getSupplierById(supplierId);

        if (supplier == null) {
            return null;
        }

        return new ArrayList<>(supplier.getOrderHistory());
    }
}
