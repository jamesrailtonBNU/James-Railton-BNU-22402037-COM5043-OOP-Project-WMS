package test;

import model.CustomerOrder;
import model.FinancialTransaction;
import model.ItemCategory;
import model.Order;
import model.OrderStatus;
import model.PurchaseOrder;
import model.TransactionStatus;
import model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FinanceService;
import service.InventoryService;
import service.OrderManager;
import service.SupplierService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderManagerTest {

	private SupplierService supplierService;
	private InventoryService inventoryService;
	private FinanceService financeService;
	private OrderManager orderManager;

	@BeforeEach
	void setUp() {
		supplierService = new SupplierService();
		supplierService.addSupplier("Galaxy Runway Tools", "tower@plane.test", "Ariane Payloads");
		inventoryService = new InventoryService();
		financeService = new FinanceService();
		inventoryService.addItem("Saturn Dock Sealant", ItemCategory.CONSUMABLES, 10, 2, 1);
		inventoryService.addItem("Bombardier Panel Drill", ItemCategory.POWER_TOOLS, 5, 1, 1);
		orderManager = new OrderManager(supplierService, inventoryService, financeService);
	}

	@Test
	void addCustomer() {
		int orderId = orderManager.addCustomerOrder(1, 2, 4.5);

		assertEquals(1, orderId);
		assertEquals(1, orderManager.getOrders().size());
		assertTrue(orderManager.getOrders().get(0) instanceof CustomerOrder);
		assertEquals(8, inventoryService.getItemById(1).getItemQuantity());
		assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.valueOf(9.0)));
	}

	@Test
	void addPurchase() {
		int orderId = orderManager.addPurchaseOrder(2, 3, 99.99, 1);

		assertEquals(1, orderId);
		assertEquals(1, orderManager.getOrders().size());
		assertTrue(orderManager.getOrders().get(0) instanceof PurchaseOrder);
		assertEquals(1, supplierService.getSupplierOrderHistory(1).size());
		assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.valueOf(-299.97)));
	}

	@Test
	void keepsIdsUniqueAcrossOrderTypes() {
		int customerOrderId = orderManager.addCustomerOrder(1, 2, 4.5);
		int purchaseOrderId = orderManager.addPurchaseOrder(2, 3, 99.99, 1);

		assertEquals(1, customerOrderId);
		assertEquals(2, purchaseOrderId);
	}

	@Test
	void changePurchaseStatus() {
		int orderId = orderManager.addPurchaseOrder(2, 3, 99.99, 1);

		assertTrue(orderManager.changeOrderStatus(orderId, OrderStatus.DELIVERED));

		Order order = orderManager.getOrders().get(0);
		assertEquals(OrderStatus.DELIVERED, order.getStatus());
		assertEquals(8, inventoryService.getItemById(2).getItemQuantity());
	}

	@Test
	void cancelCustomer() {
		int orderId = orderManager.addCustomerOrder(1, 2, 4.5);

		assertTrue(orderManager.cancelOrder(orderId));

		Order order = orderManager.getOrders().get(0);
		assertEquals(OrderStatus.CANCELLED, order.getStatus());
		assertEquals(10, inventoryService.getItemById(1).getItemQuantity());
		assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.ZERO));

		assertEquals(2, financeService.getAllTransactions().size());
		FinancialTransaction originalTransaction = financeService.getAllTransactions().get(0);
		FinancialTransaction reversalTransaction = financeService.getAllTransactions().get(1);
		assertEquals(TransactionType.INCOME, originalTransaction.getTransactionType());
		assertEquals(TransactionStatus.COMPLETED, originalTransaction.getTransactionStatus());
		assertEquals(TransactionType.REVERSAL, reversalTransaction.getTransactionType());
		assertEquals(TransactionStatus.COMPLETED, reversalTransaction.getTransactionStatus());
	}

	@Test
	void activeOrderCheckMatchesOrderState() {
		assertFalse(orderManager.hasActiveOrdersForItem(1));

		int orderId = orderManager.addCustomerOrder(1, 1, 5.0);

		assertTrue(orderManager.hasActiveOrdersForItem(1));
		assertFalse(orderManager.hasActiveOrdersForItem(999));

		assertTrue(orderManager.cancelOrder(orderId));
		assertFalse(orderManager.hasActiveOrdersForItem(1));
	}

}