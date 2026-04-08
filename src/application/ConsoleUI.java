package application;

import controller.WmsController;
import model.ExpenseReport;
import model.FinancialReport;
import model.FinancialTransaction;
import model.InventoryItem;
import model.ItemCategory;
import model.ItemMovementRecord;
import model.Order;
import model.ReportType;
import model.SalesReport;
import model.Supplier;
import utlities.Validation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;


// This is used for console-based user interface for the WMS application It provides a menu-driven interface that allows users to interact with the system's features
public class ConsoleUI {
    private static final DateTimeFormatter MOVEMENT_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TRANSACTION_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private boolean running = true;
    private final WmsController controller = new WmsController();

    public void run() {
        System.out.println();
        System.out.println("BNU Industry Solutions Ltd WMS 1.1");


        while (running) {
            System.out.println();
            showMainMenu();
            int choice = Validation.getValidatedInt("Please Select an option: ");

            switch (choice) {
                case 1:
                    supplierMenu();
                    break;
                case 2:
                    inventoryMenu();
                    break;
                case 3:
                    orderMenu();
                    break;
                case 4:
                    financeMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting system good Bye!");
                    break;
                default:
                    System.out.println("Invalid option, Please choose 0 to 4.\n");
            }
        }
    }

    private void showMainMenu() {
        printTitle("MAIN MENU");
        System.out.println("1: Supplier Management");
        System.out.println("2: Inventory Management");
        System.out.println("3: Order Processing");
        System.out.println("4: Finance Management");
        System.out.println("0: Exit");
    }

    /* Supplier */

    private void supplierMenu() {
        boolean inSupplierMenu = true;

        while (inSupplierMenu) {
            System.out.println();
            printTitle("SUPPLIER MENU");
            System.out.println("1: Add Supplier");
            System.out.println("2: View Suppliers");
            System.out.println("3: Remove Supplier");
            System.out.println("4: Edit Supplier");
            System.out.println("5: View Supplier Order History");
            System.out.println("0. Back");

            int select = Validation.getValidatedInt("Please Select an option: ");

            switch (select) {
                case 1:
                    addSupplierUI();
                    break;
                case 2:
                    viewSuppliersUI();
                    break;
                case 3:
                    removeSupplierUI();
                    break;
                case 4:
                    editSupplierUI();
                    break;
                case 5:
                    viewSupplierOrderHistoryUI();
                    break;
                case 0:
                    inSupplierMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 0 to 5.\n");
                    break;
            }
        }
    }

    private void addSupplierUI() {
        String name = Validation.getValidatedString("Enter supplier name: ");
        String contact = Validation.getValidatedString("Enter contact information: ");
        String itemType = Validation.getValidatedString("Enter type of items supplied: ");

        controller.addSupplier(name, contact, itemType);
        System.out.println("Supplier added successfully.\n");
    }

    private void removeSupplierUI() {
        int supplierId = Validation.getValidatedInt("Enter supplier ID to remove: ");
        if (supplierId <= 0) {
            System.out.println("Supplier ID must be greater than 0.\n");
            return;
        }

        boolean removed = controller.deleteSupplier(supplierId);

        if (removed) {
            System.out.println("Supplier removed successfully.\n");
        } else {
            System.out.println("Supplier not found.\n");
        }
    }

    private void editSupplierUI() {
        int supplierId = Validation.getValidatedInt("Enter supplier ID to edit: ");
        if (supplierId <= 0) {
            System.out.println("Supplier ID must be greater than 0.\n");
            return;
        }

        String name = Validation.getValidatedString("Enter new supplier name: ");
        String contact = Validation.getValidatedString("Enter new contact information: ");
        String itemType = Validation.getValidatedString("Enter new type of items supplied: ");
        boolean updated = controller.updateSupplier(supplierId, name, contact, itemType);

        if (updated) {
            System.out.println("Supplier updated successfully.\n");
        } else {
            System.out.println("Supplier not found.\n");
        }
    }

    private void viewSuppliersUI() {
        System.out.println();
        printTitle("SUPPLIERS");
        printSuppliers(controller.getAllSuppliers());
        System.out.println();
    }

    private void viewSupplierOrderHistoryUI() {
        int supplierId = Validation.getValidatedInt("Enter supplier ID to view order history: ");
        if (supplierId <= 0) {
            System.out.println("Supplier ID must be greater than 0.\n");
            return;
        }

        Supplier supplier = getSupplierById(supplierId);

        if (supplier == null) {
            System.out.println("Supplier not found.\n");
            return;
        }

        List<String> orderHistory = controller.getSupplierOrderHistory(supplierId);

        System.out.println();
        printTitle("ORDER HISTORY FOR SUPPLIER ID " + supplierId);
        System.out.println("Supplier: " + supplier.getName());

        if (orderHistory == null || orderHistory.isEmpty()) {
            System.out.println("No order history found for this supplier.\n");
            return;
        }

        for (String entry : orderHistory) {
            System.out.println("- " + entry);
        }
        System.out.println();
    }


    /* Inventory */

    private void inventoryMenu() {
        boolean inInventoryMenu = true;

        while (inInventoryMenu) {
            System.out.println();
            printTitle("INVENTORY MENU");
            System.out.println("1: Add Stock Item");
            System.out.println("2: View Inventory Stock");
            System.out.println("3: Adjust Inventory Stock");
            System.out.println("4: Delete Stock Item");
            System.out.println("5: View Stock Item Movement History");
            System.out.println("6: View Low Stock Items");
            System.out.println("0. Back");

            int select = Validation.getValidatedInt("Please Select an option: ");

            switch (select) {
                case 1:
                    addStockItemUI();
                    break;
                case 2:
                    viewStockItemsUI();
                    break;
                case 3:
                    manageStockMenu();
                    break;
                case 4:
                    deleteStockItemUI();
                    break;
                case 5:
                    viewItemMovementHistoryUI();
                    break;
                case 6:
                    viewLowStockItemsUI();
                    break;
                case 0:
                    inInventoryMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 0 to 6.\n");
                    break;
            }
        }
    }

    private void addStockItemUI() {
        String itemName = Validation.getValidatedString("Enter item name: ");
        ItemCategory itemCategory = promptItemCategory();
        int initialQuantity = Validation.getValidatedInt("Enter initial quantity: ");
        int lowStockLevel = Validation.getValidatedInt("Enter low stock level: ");
        int supplierId = promptOptionalSupplierId();

        if (initialQuantity < 0 || lowStockLevel < 0) {
            System.out.println("Initial quantity and low stock level cannot be negative.\n");
            return;
        }

        Integer itemId = controller.addStockItem(itemName, itemCategory, initialQuantity, lowStockLevel, supplierId);

        if (itemId != null) {
            System.out.println("Stock item added successfully. Item ID: " + itemId + "\n");
        } else {
            System.out.println("Item could not be added. Check the details and supplier selection.\n");
        }
    }

    private void viewStockItemsUI() {
        System.out.println();
        printTitle("INVENTORY");
        printStockItems(controller.getStockItems());
        System.out.println();
    }

    private void manageStockMenu() {
        boolean inStockMenu = true;

        while (inStockMenu) {
            System.out.println();
            printTitle("ADJUST STOCK");
            System.out.println("1: Add Stock");
            System.out.println("2: Remove Stock");
            System.out.println("0: Back");

            int select = Validation.getValidatedInt("Please Select an option: ");

            switch (select) {
                case 1:
                    addStockUI();
                    break;
                case 2:
                    removeStockUI();
                    break;
                case 0:
                    inStockMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 0 to 2.\n");
                    break;
            }
        }
    }

    private void addStockUI() {
        if (hasNoStockItems()) {
            return;
        }

        printStockItems(controller.getStockItems());
        int itemId = Validation.getValidatedInt("Enter item ID: ");
        int quantity = Validation.getValidatedInt("Enter quantity to add: ");
        String note = Validation.getOptionalString("Enter note (optional): ");

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.\n");
            return;
        }

        boolean updated = controller.addStock(itemId, quantity, note);

        if (updated) {
            System.out.println("Stock added successfully.\n");
        } else {
            System.out.println("Stock addition failed. Check the item ID and quantity.\n");
        }
    }

    private void removeStockUI() {
        if (hasNoStockItems()) {
            return;
        }

        printStockItems(controller.getStockItems());
        int itemId = Validation.getValidatedInt("Enter item ID: ");
        int quantity = Validation.getValidatedInt("Enter quantity to remove: ");
        String note = Validation.getOptionalString("Enter note (optional): ");

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.\n");
            return;
        }

        boolean updated = controller.removeStock(itemId, quantity, note);

        if (updated) {
            System.out.println("Stock removed successfully.\n");
        } else {
            System.out.println("Stock removal failed. Check the item ID, quantity, and available stock.\n");
        }
    }

    private void deleteStockItemUI() {
        if (hasNoStockItems()) {
            return;
        }

        System.out.println();
        printTitle("DELETE STOCK ITEM");
        printStockItems(controller.getStockItems());

        int itemId = Validation.getValidatedInt("Enter item ID to delete: ");

        if (itemId <= 0) {
            System.out.println("Item ID must be greater than 0.\n");
            return;
        }

        boolean deleted = controller.deleteStockItem(itemId);

        if (deleted) {
            System.out.println("Stock item deleted successfully.\n");
        } else {
            System.out.println("Stock item could not be deleted. Check the item ID and ensure there are no active orders for this item.\n");
        }
    }

    private void viewItemMovementHistoryUI() {
        if (hasNoStockItems()) {
            return;
        }

        printStockItems(controller.getStockItems());
        int itemId = Validation.getValidatedInt("Enter item ID to view movement history: ");
        InventoryItem item = controller.getStockItem(itemId);

        if (item == null) {
            System.out.println("Stock item not found.\n");
            return;
        }

        System.out.println();
        printTitle("INVENTORY MOVEMENT HISTORY FOR ITEM " + item.getItemId());
        System.out.println("Item: " + item.getItemName());

        if (item.getStockMovementHistory().isEmpty()) {
            System.out.println("No movement history found for this item.\n");
            return;
        }

        System.out.printf("%-20s %-12s %-10s %-24s%n", "TIMESTAMP", "TYPE", "QTY", "NOTE");
        for (ItemMovementRecord movement : item.getStockMovementHistory()) {
            System.out.printf("%-20s %-12s %-10d %-24s%n",
                    movement.getTimestamp().format(MOVEMENT_TIME_FORMAT),
                    movement.getMovementType(),
                    movement.getQuantity(),
                    movement.getNote().isEmpty() ? "-" : movement.getNote());
        }
        System.out.println();
    }

    private void viewLowStockItemsUI() {
        System.out.println();
        printTitle("LOW STOCK ITEMS");
        List<InventoryItem> lowStockItems = controller.getLowStockItems();

        if (lowStockItems.isEmpty()) {
            System.out.println("No low stock items found.\n");
            return;
        }

        printStockItems(lowStockItems);
        System.out.println();
    }

    private ItemCategory promptItemCategory() {
        ItemCategory[] categories = ItemCategory.values();

        while (true) {
            System.out.println();
            printTitle("ITEM CATEGORIES");
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ": " + categories[i].name().replace('_', ' '));
            }

            int select = Validation.getValidatedInt("Select a category: ");

            if (select >= 1 && select <= categories.length) {
                return categories[select - 1];
            }

            System.out.println("Invalid category. Please try again.");
        }
    }

    private int promptOptionalSupplierId() {
        List<Supplier> suppliers = controller.getAllSuppliers();

        if (suppliers.isEmpty()) {
            System.out.println("No suppliers available. Item will be created without a linked supplier.");
            return 0;
        }

        System.out.println();
        printTitle("AVAILABLE SUPPLIERS");
        printSuppliers(suppliers);
        System.out.println("Enter 0 if this stock item is not linked to a supplier.");
        return Validation.getValidatedInt("Enter supplier ID (0 for none): ");
    }

    private boolean hasNoStockItems() {
        if (controller.getStockItems().isEmpty()) {
            System.out.println("No stock items found. Add a stock item first.\n");
            return true;
        }
        return false;
    }

    private Supplier getSupplierById(int supplierId) {
        for (Supplier supplier : controller.getAllSuppliers()) {
            if (supplier.getId() == supplierId) {
                return supplier;
            }
        }

        return null;
    }

    private void printSuppliers(List<Supplier> suppliers) {
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
            return;
        }

        System.out.printf("%-6s %-22s %-28s %-22s%n", "ID", "NAME", "CONTACT", "ITEMS");
        for (Supplier supplier : suppliers) {
            System.out.printf("%-6d %-22s %-28s %-22s%n",
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getContactInfo(),
                    supplier.getItemType());
        }
    }

    private void printOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.printf("%-6s %-18s %-8s %-18s %-8s %-14s %-12s %-10s%n",
                "ID", "TYPE", "ID", "ITEM", "QTY", "STATUS", "TOTAL", "SUPPLIER");

        for (Order order : orders) {
            System.out.printf("%-6d %-18s %-8d %-18s %-8d %-14s £%-11.2f %-10s%n",
                    order.getOrderId(),
                    order.getOrderType(),
                    order.getItemId(),
                    order.getItemName(),
                    order.getQuantity(),
                    order.getStatus(),
                    order.calculateTotal(),
                    order.getSupplierDisplay());
        }
    }

    private void printStockItems(List<InventoryItem> items) {
        if (items.isEmpty()) {
            System.out.println("No stock items found.");
            return;
        }

        System.out.printf("%-12s %-24s %-26s %-10s %-20s %-18s %-10s%n", "ID", "NAME", "CATEGORY", "STOCK", "LOW STOCK LEVEL", "SUPPLIER", "LOW?");
        for (InventoryItem item : items) {
            System.out.printf("%-12d %-24s %-26s %-10d %-20d %-18s %-10s%n",
                    item.getItemId(),
                    item.getItemName(),
                    item.getItemCategory(),
                    item.getStockQuantity(),
                    item.getLowStockLevel(),
                    controller.getSupplierName(item.getSupplierId()),
                    item.isLowStock() ? "YES" : "NO");
        }
    }


    /* Orders */

    private void orderMenu() {
        boolean inOrderMenu = true;

        while (inOrderMenu) {
            System.out.println();
            printTitle("ORDER MENU");
            System.out.println("1: Add Customer Order");
            System.out.println("2: Add Purchase Order");
            System.out.println("3: View Orders");
            System.out.println("4: Edit Order Status");
            System.out.println("5: Cancel Order");
            System.out.println("0. Back");

            int choice = Validation.getValidatedInt("Please Select an option: ");

            switch (choice) {
                case 1:
                    addCustomerOrderUI();
                    break;
                case 2:
                    addPurchaseOrderUI();
                    break;
                case 3:
                    viewOrdersUI();
                    break;
                case 4:
                    editOrderStatusUI();
                    break;
                case 5:
                    cancelOrderUI();
                    break;
                case 0:
                    inOrderMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 0 to 5.\n");
                    break;
            }
        }
    }

    private void addCustomerOrderUI() {
        if (hasNoStockItems()) {
            return;
        }

        System.out.println();
        printTitle("AVAILABLE INVENTORY FOR CUSTOMER ORDERS");
        printStockItems(controller.getStockItems());

        int itemId = Validation.getValidatedInt("Enter item ID: ");
        int quantity = Validation.getValidatedInt("Enter quantity: ");
        double itemPrice = Validation.getValidatedDouble("Enter item price: ");

        if (quantity <= 0 || itemPrice < 0) {
            System.out.println("Quantity must be greater than 0 and price cannot be negative.\n");
            return;
        }

        int orderId = controller.addCustomerOrder(itemId, quantity, itemPrice);

        if (orderId == -1) {
            System.out.println("Customer order could not be created. Ensure the item exists and enough stock is available.\n");
        } else {
            System.out.println("Customer order added successfully. Order ID: " + orderId + "\n");
        }
    }

    private void addPurchaseOrderUI() {
        if (hasNoStockItems()) {
            return;
        }

        List<Supplier> suppliers = controller.getAllSuppliers();

        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found. Add a supplier before creating a purchase order.\n");
            return;
        }

        System.out.println();
        printTitle("AVAILABLE INVENTORY FOR PURCHASE ORDERS");
        printStockItems(controller.getStockItems());
        System.out.println();
        printTitle("AVAILABLE SUPPLIERS");
        printSuppliers(suppliers);

        int itemId = Validation.getValidatedInt("Enter item ID: ");
        int quantity = Validation.getValidatedInt("Enter quantity: ");
        double itemPrice = Validation.getValidatedDouble("Enter item price: ");
        int supplierId = Validation.getValidatedInt("Enter supplier ID: ");

        if (quantity <= 0 || itemPrice < 0 || supplierId <= 0) {
            System.out.println("Quantity must be greater than 0, price cannot be negative, and supplier ID must be greater than 0.\n");
            return;
        }

        int orderId = controller.addPurchaseOrder(itemId, quantity, itemPrice, supplierId);

        if (orderId == -1) {
            System.out.println("Purchase order could not be created. Check the item, supplier, and linked supplier details.\n");
        } else {
            System.out.println("Purchase order added successfully. Order ID: " + orderId + "\n");
        }
    }

    private void viewOrdersUI() {
        System.out.println();
        printTitle("ORDERS");
        printOrders(controller.getOrders());
        System.out.println();
    }

    private void editOrderStatusUI() {
        int orderId = Validation.getValidatedInt("Enter order ID to edit status: ");

        if (orderId <= 0) {
            System.out.println("Order ID must be greater than 0.\n");
            return;
        }

        System.out.println();
        printTitle("EDIT ORDER STATUS");
        System.out.println("1: Pending");
        System.out.println("2: In Transit");
        System.out.println("3: Processed");
        System.out.println("4: Delivered");
        System.out.println("5: Delayed");
        System.out.println("0: Back");

        int statusChoice = Validation.getValidatedInt("Please Select a status: ");

        if (statusChoice == 0) {
            return;
        }

        boolean updated = controller.updateOrderStatus(orderId, statusChoice);

        if (updated) {
            System.out.println("Order status updated successfully.\n");
        } else {
            System.out.println("Order not found or status change not allowed.\n");
        }
    }

    private void cancelOrderUI() {
        int orderId = Validation.getValidatedInt("Enter order ID to cancel: ");

        if (orderId <= 0) {
            System.out.println("Order ID must be greater than 0.\n");
            return;
        }

        boolean cancelled = controller.cancelOrder(orderId);

        if (cancelled) {
            System.out.println("Order cancelled successfully.\n");
        } else {
            System.out.println("Order not found, already cancelled, or can no longer be cancelled.\n");
        }
    }

    /* Finance */

    private void financeMenu() {
        boolean inFinanceMenu = true;

        while (inFinanceMenu) {
            System.out.println();
            printTitle("FINANCE MENU");
            System.out.println("1: View Account Balance");
            System.out.println("2: View All Transactions");
            System.out.println("3: Generate Reports");
            System.out.println("0. Back");

            int choice = Validation.getValidatedInt("Please Select an option: ");

            switch (choice) {
                case 1:
                    viewAccountBalanceUI();
                    break;
                case 2:
                    viewAllTransactionsUI();
                    break;
                case 3:
                    reportMenu();
                    break;
                case 0:
                    inFinanceMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 0 to 3.\n");
                    break;
            }
        }
    }

    private void reportMenu() {
        boolean inReportMenu = true;

        while (inReportMenu) {
            System.out.println();
            printTitle("GENERATE REPORTS");
            System.out.println("1: Sales Report");
            System.out.println("2: Expense Report");
            System.out.println("0: Back");

            int choice = Validation.getValidatedInt("Please Select an option: ");

            switch (choice) {
                case 1:
                    generateSalesReportUI();
                    break;
                case 2:
                    generateExpenseReportUI();
                    break;
                case 0:
                    inReportMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 0 to 2.\n");
                    break;
            }
        }
    }

    private void viewAccountBalanceUI() {
        System.out.println();
        printTitle("ACCOUNT BALANCE");
        System.out.println("Current Balance: " + formatCurrency(controller.getAccountBalance()));
        System.out.println();
    }

    private void viewAllTransactionsUI() {
        System.out.println();
        printTitle("ALL TRANSACTIONS");
        printFinancialTransactions(controller.getAllTransactions());
        System.out.println();
    }

    private void generateSalesReportUI() {
        ReportType reportType = promptReportType();
        if (reportType == null) {
            return;
        }

        SalesReport report = controller.generateSalesReport(reportType);
        if (report == null) {
            System.out.println("Sales report could not be generated.\n");
            return;
        }

        System.out.println();
        printTitle("SALES REPORT");
        printSalesReport(report);
        System.out.println();
    }

    private void generateExpenseReportUI() {
        ReportType reportType = promptReportType();
        if (reportType == null) {
            return;
        }

        ExpenseReport report = controller.generateExpenseReport(reportType);
        if (report == null) {
            System.out.println("Expense report could not be generated.\n");
            return;
        }

        System.out.println();
        printTitle("EXPENSE REPORT");
        printExpenseReport(report);
        System.out.println();
    }

    private ReportType promptReportType() {
        ReportType[] reportTypes = ReportType.values();

        while (true) {
            System.out.println();
            printTitle("REPORT TYPES");
            for (int i = 0; i < reportTypes.length; i++) {
                System.out.println((i + 1) + ": " + reportTypes[i].name().replace('_', ' '));
            }
            System.out.println("0: Back");

            int choice = Validation.getValidatedInt("Select a report type: ");

            if (choice == 0) {
                return null;
            }

            if (choice >= 1 && choice <= reportTypes.length) {
                return reportTypes[choice - 1];
            }

            System.out.println("Invalid report type. Please try again.");
        }
    }

    private void printFinancialTransactions(List<FinancialTransaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No financial transactions found.");
            return;
        }

        System.out.printf("%-6s %-10s %-20s %-12s %-14s %-12s%n",
                "ID", "ORDER ID", "TIMESTAMP", "TYPE", "AMOUNT", "STATUS");

        for (FinancialTransaction transaction : transactions) {
            System.out.printf("%-6d %-10d %-20s %-12s %-14s %-12s%n",
                    transaction.getTransactionId(),
                    transaction.getOrderId(),
                    transaction.getTransactionDateTime().format(TRANSACTION_TIME_FORMAT),
                    transaction.getTransactionType(),
                    formatCurrency(transaction.getTransactionAmount()),
                    transaction.getTransactionStatus());
        }
    }

    private void printSalesReport(SalesReport report) {
        printFinancialReportSummary(report);
        System.out.println("Income Transactions: " + report.getIncomeTransactionCount());
        System.out.println("Total Income: " + formatCurrency(report.getTotalIncome()));
        System.out.println();
        printTitle("REPORT TRANSACTION HISTORY");
        printFinancialTransactions(report.getTransactionHistory());
    }

    private void printExpenseReport(ExpenseReport report) {
        printFinancialReportSummary(report);
        System.out.println("Expense Transactions: " + report.getExpenseTransactionCount());
        System.out.println("Total Expenses: " + formatCurrency(report.getTotalExpenses()));
        System.out.println();
        printTitle("REPORT TRANSACTION HISTORY");
        printFinancialTransactions(report.getTransactionHistory());
    }

    private void printFinancialReportSummary(FinancialReport report) {
        System.out.println("Report ID: " + report.getReportId());
        System.out.println("Report Type: " + report.getReportType());
        System.out.println("Start Date: " + report.getStartDate());
        System.out.println("End Date: " + report.getEndDate());
        System.out.println("Generated At: " + report.getReportGeneratedAt().format(TRANSACTION_TIME_FORMAT));
        System.out.println("Reversal Transactions: " + report.getReversalTransactionCount());
        System.out.println("Total Reversals: " + formatCurrency(report.getTotalReversals()));
        System.out.println("Net Amount: " + formatCurrency(report.getNetAmount()));
    }

    private String formatCurrency(BigDecimal amount) {
        return "£" + amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private void printTitle(String title) {
        System.out.println("|~~~ " + title + " ~~~|");
    }
}
