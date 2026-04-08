package test;

import model.FinancialTransaction;
import model.TransactionStatus;
import model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FinancialTransactionTest {

	private FinancialTransaction financialTransaction;

	@BeforeEach
	void setUp() {
		financialTransaction = new FinancialTransaction(
				1,
				101,
				LocalDateTime.of(2026, 4, 8, 9, 45),
				BigDecimal.valueOf(787.25),
				TransactionType.INCOME);
	}

	@Test
	void getTransactionId() {
		assertEquals(1, financialTransaction.getTransactionId());
	}

	@Test
	void getOrderId() {
		assertEquals(101, financialTransaction.getOrderId());
	}

	@Test
	void orderId() {
		assertEquals(101, financialTransaction.orderId());
	}

	@Test
	void getTransactionDateTime() {
		assertEquals(LocalDateTime.of(2026, 4, 8, 9, 45), financialTransaction.getTransactionDateTime());
	}

	@Test
	void getTransactionAmount() {
		assertEquals(0, financialTransaction.getTransactionAmount().compareTo(BigDecimal.valueOf(787.25)));
	}

	@Test
	void getTransactionType() {
		assertEquals(TransactionType.INCOME, financialTransaction.getTransactionType());
	}

	@Test
	void getTransactionStatus() {
		assertEquals(TransactionStatus.COMPLETED, financialTransaction.getTransactionStatus());
	}

	@Test
	void isActive() {
		assertTrue(financialTransaction.isActive());
		financialTransaction.voidTransaction();
		assertFalse(financialTransaction.isActive());
	}

	@Test
	void voidTransaction() {
		assertTrue(financialTransaction.voidTransaction());
		assertEquals(TransactionStatus.VOIDED, financialTransaction.getTransactionStatus());
		assertFalse(financialTransaction.voidTransaction());
	}
}

