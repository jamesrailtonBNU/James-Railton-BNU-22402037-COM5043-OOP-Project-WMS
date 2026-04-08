package test;

import model.ExpenseReport;
import model.FinancialTransaction;
import model.ReportType;
import model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseReportTest {

    private ExpenseReport expenseReport;

    @BeforeEach
    void setUp() {
        FinancialTransaction runwayTransaction = new FinancialTransaction(
                1,
                301,
                LocalDateTime.of(2026, 4, 8, 13, 30),
                BigDecimal.valueOf(500.00),
                TransactionType.EXPENSE);

        expenseReport = new ExpenseReport(
                1,
                ReportType.WEEKLY,
                LocalDate.of(2026, 4, 1),
                LocalDateTime.of(2026, 4, 8, 18, 0),
                LocalDate.of(2026, 4, 8),
                1,
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(-450.00),
                2,
                BigDecimal.valueOf(500.00),
                List.of(runwayTransaction));
    }

    @Test
    void makeExpenseReport() {
        assertEquals(1, expenseReport.getReportId());
        assertEquals(ReportType.WEEKLY, expenseReport.getReportType());
        assertEquals(LocalDate.of(2026, 4, 1), expenseReport.getStartDate());
        assertEquals(LocalDateTime.of(2026, 4, 8, 18, 0), expenseReport.getReportGeneratedAt());
        assertEquals(LocalDate.of(2026, 4, 8), expenseReport.getEndDate());
        assertEquals(1, expenseReport.getReversalTransactionCount());
        assertEquals(0, expenseReport.getTotalReversals().compareTo(BigDecimal.valueOf(50.00)));
        assertEquals(0, expenseReport.getNetAmount().compareTo(BigDecimal.valueOf(-450.00)));
        assertEquals(1, expenseReport.getTransactionHistory().size());
    }

    @Test
    void getExpenseTransactionCount() {
        assertEquals(2, expenseReport.getExpenseTransactionCount());
    }

    @Test
    void getTotalExpenses() {
        assertEquals(0, expenseReport.getTotalExpenses().compareTo(BigDecimal.valueOf(500.00)));
    }

    @Test
    void getTransactionHistory() {
        assertEquals(1, expenseReport.getTransactionHistory().size());
        assertEquals(301, expenseReport.getTransactionHistory().getFirst().getOrderId());
    }
}