package test;

import model.ExpenseReport;
import model.FinancialTransaction;
import model.ReportType;
import model.SalesReport;
import model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FinanceService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinanceServiceTest {

	private FinanceService financeService;

	@BeforeEach
	void setUp() {
		financeService = new FinanceService();
	}

	@Test
	void addIncome() {
		FinancialTransaction transaction = financeService.recordIncome(11, BigDecimal.valueOf(747.50));

		assertNotNull(transaction);
		assertEquals(1, transaction.getTransactionId());
		assertEquals(11, transaction.getOrderId());
		assertEquals(TransactionType.INCOME, transaction.getTransactionType());
		assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.valueOf(747.50)));
	}

	@Test
	void addExpense() {
		FinancialTransaction transaction = financeService.recordExpense(22, BigDecimal.valueOf(320.10));

		assertNotNull(transaction);
		assertEquals(1, transaction.getTransactionId());
		assertEquals(TransactionType.EXPENSE, transaction.getTransactionType());
		assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.valueOf(-320.10)));
	}

	@Test
	void addTransaction() {
		FinancialTransaction apolloIncome = financeService.recordTransaction(31, BigDecimal.valueOf(1200.75), TransactionType.INCOME);
		FinancialTransaction runwayExpense = financeService.recordTransaction(32, BigDecimal.valueOf(450.25), TransactionType.EXPENSE);

		assertNotNull(apolloIncome);
		assertNotNull(runwayExpense);
		assertEquals(1, apolloIncome.getTransactionId());
		assertEquals(2, runwayExpense.getTransactionId());
		assertEquals(0, financeService.getAccountBalance().compareTo(BigDecimal.valueOf(750.50)));
	}

	@Test
	void rejectInvalidTransaction() {
		assertNull(financeService.recordTransaction(0, BigDecimal.valueOf(99.99), TransactionType.INCOME));
		assertNull(financeService.recordTransaction(7, null, TransactionType.INCOME));
		assertNull(financeService.recordTransaction(7, BigDecimal.ZERO, TransactionType.INCOME));
		assertNull(financeService.recordTransaction(7, BigDecimal.valueOf(-10.00), TransactionType.INCOME));
		assertNull(financeService.recordTransaction(7, BigDecimal.valueOf(10.00), null));
		assertNull(financeService.recordTransaction(7, BigDecimal.valueOf(10.00), TransactionType.REVERSAL));
		assertTrue(financeService.getAllTransactions().isEmpty());
	}

	@Test
	void readTransactions() {
		financeService.recordIncome(41, BigDecimal.valueOf(500.00));
		financeService.recordExpense(42, BigDecimal.valueOf(125.00));

		List<FinancialTransaction> transactions = financeService.getAllTransactions();
		assertEquals(2, transactions.size());

		transactions.clear();
		assertEquals(2, financeService.getAllTransactions().size());
	}

	@Test
	void getRecentTransactions() {
		FinancialTransaction cargoIncome = financeService.recordIncome(51, BigDecimal.valueOf(999.99));

		List<FinancialTransaction> recentTransactions = financeService.getRecentTransactions();

		assertEquals(1, recentTransactions.size());
		assertEquals(cargoIncome.getTransactionId(), recentTransactions.getFirst().getTransactionId());
	}

	@Test
	void generateSales() {
		financeService.recordIncome(61, BigDecimal.valueOf(850.00));
		financeService.recordIncome(62, BigDecimal.valueOf(150.00));
		financeService.recordExpense(63, BigDecimal.valueOf(200.00));

		SalesReport salesReport = financeService.generateSalesReport(ReportType.WEEKLY);

		assertNotNull(salesReport);
		assertEquals(1, salesReport.getReportId());
		assertEquals(ReportType.WEEKLY, salesReport.getReportType());
		assertEquals(2, salesReport.getIncomeTransactionCount());
		assertEquals(0, salesReport.getTotalIncome().compareTo(BigDecimal.valueOf(1000.00)));
		assertEquals(0, salesReport.getNetAmount().compareTo(BigDecimal.valueOf(800.00)));
		assertEquals(0, salesReport.getTotalReversals().compareTo(BigDecimal.ZERO));
		assertEquals(0, salesReport.getReversalTransactionCount());
		assertEquals(3, salesReport.getTransactionHistory().size());
	}

	@Test
	void generateExpenses() {
		financeService.recordExpense(71, BigDecimal.valueOf(600.00));
		financeService.recordIncome(72, BigDecimal.valueOf(150.00));

		ExpenseReport expenseReport = financeService.generateExpenseReport(ReportType.DAILY);

		assertNotNull(expenseReport);
		assertEquals(1, expenseReport.getReportId());
		assertEquals(ReportType.DAILY, expenseReport.getReportType());
		assertEquals(1, expenseReport.getExpenseTransactionCount());
		assertEquals(0, expenseReport.getTotalExpenses().compareTo(BigDecimal.valueOf(600.00)));
		assertEquals(0, expenseReport.getNetAmount().compareTo(BigDecimal.valueOf(-450.00)));
		assertEquals(0, expenseReport.getTotalReversals().compareTo(BigDecimal.ZERO));
		assertEquals(0, expenseReport.getReversalTransactionCount());
		assertEquals(2, expenseReport.getTransactionHistory().size());
	}

	@Test
	void rejectNullReportType() {
		assertNull(financeService.generateSalesReport(null));
		assertNull(financeService.generateExpenseReport(null));
	}
}

