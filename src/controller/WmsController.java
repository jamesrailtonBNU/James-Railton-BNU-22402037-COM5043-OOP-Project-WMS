package controller;

import model.ExpenseReport;
import model.FinancialTransaction;
import model.Order;
import model.OrderStatus;
import model.InventoryItem;
import model.ItemCategory;
import model.ReportType;
import model.SalesReport;
import model.Supplier;
import service.FinanceService;
import service.InventoryService;
import service.OrderManager;
import service.SupplierService;
import java.math.BigDecimal;
import java.util.List;

public class WmsController {
    private final SupplierService supplierService;
    private final OrderManager orderManager;
    private final InventoryService inventoryService;
    private final FinanceService financeService;

    /*
     * The WmsController acts as a link between the user interface and service layer of application
     * Prevents the user interface from directly accessing the service layer, ensuring a clear separation of concerns and better maintainability.
     * */

    public WmsController() {
        supplierService = new SupplierService();
        inventoryService = new InventoryService();
        financeService = new FinanceService();
        orderManager = new OrderManager(supplierService, inventoryService, financeService);
    }

    /* Suppliers */

    public void addSupplier(String name, String contactInfo, String itemType) {
        supplierService.addSupplier(name, contactInfo, itemType);
    }

    public boolean updateSupplier(int supplierId, String name, String contactInfo, String itemType) {
        return supplierService.updateSupplier(supplierId, name, contactInfo, itemType);
    }

    public boolean deleteSupplier(int supplierId) {
        return supplierService.deleteSupplier(supplierId);
    }


    /* Inventory */

    public Integer addStockItem(String itemName, ItemCategory itemCategory, int initialQuantity, int lowStockLevel, int supplierId) {
        if (supplierId > 0 && !supplierService.supplierExists(supplierId)) {
            return null;
        }

        InventoryItem item = inventoryService.addItem(itemName, itemCategory, initialQuantity, lowStockLevel, supplierId);
        return item == null ? null : item.getItemId();
    }

    public List<InventoryItem> getStockItems() {
        return inventoryService.getAllItems();
    }

    public boolean addStock(int itemId, int quantity, String note) {
        return inventoryService.addStock(itemId, quantity, note);
    }

    public boolean removeStock(int itemId, int quantity, String note) {
        return inventoryService.removeStock(itemId, quantity, note);
    }

    public boolean deleteStockItem(int itemId) {
        if (itemId <= 0) {
            return false;
        }

        if (orderManager.hasActiveOrdersForItem(itemId)) {
            return false;
        }

        return inventoryService.deleteItem(itemId);
    }

    public InventoryItem getStockItem(int itemId) {
        return inventoryService.getItemById(itemId);
    }

    public List<InventoryItem> getLowStockItems() {
        return inventoryService.getLowStockItems();
    }

    public String getSupplierName(int supplierId) {
        if (supplierId <= 0) {
            return "-";
        }

        Supplier supplier = supplierService.getSupplierById(supplierId);
        return supplier == null ? "Unknown" : supplier.getName();
    }

    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    /* Orders */

    public List<String> getSupplierOrderHistory(int supplierId) {
        return supplierService.getSupplierOrderHistory(supplierId);
    }

    public int addCustomerOrder(int itemId, int quantity, double itemPrice) {
        return orderManager.addCustomerOrder(itemId, quantity, itemPrice);
    }

    public int addPurchaseOrder(int itemId, int quantity, double itemPrice, int supplierId) {
        return orderManager.addPurchaseOrder(itemId, quantity, itemPrice, supplierId);
    }

    public List<Order> getOrders() {
        return orderManager.getOrders();
    }

    public boolean updateOrderStatus(int orderId, int statusChoice) {
        OrderStatus status = getOrderStatusFromChoice(statusChoice);
        if (status == null) {
            return false;
        }
        return orderManager.changeOrderStatus(orderId, status);
    }

    public boolean cancelOrder(int orderId) {
        return orderManager.cancelOrder(orderId);
    }

    private OrderStatus getOrderStatusFromChoice(int select) {
        return switch (select) {
            case 1 -> OrderStatus.PENDING;
            case 2 -> OrderStatus.IN_TRANSIT;
            case 3 -> OrderStatus.PROCESSED;
            case 4 -> OrderStatus.DELIVERED;
            case 5 -> OrderStatus.DELAYED;
            default -> null;
        };
    }

    /* finance */

    public BigDecimal getAccountBalance() {
        return financeService.getAccountBalance();
    }

    public List<FinancialTransaction> getAllTransactions() {
        return financeService.getAllTransactions();
    }


    public SalesReport generateSalesReport(ReportType reportType) {
        return financeService.generateSalesReport(reportType);
    }

    public ExpenseReport generateExpenseReport(ReportType reportType) {
        return financeService.generateExpenseReport(reportType);
    }

}
