package test;

import model.FinancialTransaction;
import model.ReportType;
import model.SalesReport;
import model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesReportTest {

    private SalesReport salesReport;

    @BeforeEach
    void setUp() {
        FinancialTransaction orbitTransaction = new FinancialTransaction(
                1,
                401,
                LocalDateTime.of(2026, 4, 8, 10, 15),
                BigDecimal.valueOf(900.00),
                TransactionType.INCOME);

        salesReport = new SalesReport(
                1,
                ReportType.DAILY,
                LocalDate.of(2026, 4, 8),
                LocalDateTime.of(2026, 4, 8, 19, 0),
                LocalDate.of(2026, 4, 8),
                1,
                BigDecimal.valueOf(75.00),
                BigDecimal.valueOf(825.00),
                3,
                BigDecimal.valueOf(900.00),
                List.of(orbitTransaction));
    }

    @Test
    void makeSalesReport() {
        assertEquals(1, salesReport.getReportId());
        assertEquals(ReportType.DAILY, salesReport.getReportType());
        assertEquals(LocalDate.of(2026, 4, 8), salesReport.getStartDate());
        assertEquals(LocalDateTime.of(2026, 4, 8, 19, 0), salesReport.getReportGeneratedAt());
        assertEquals(LocalDate.of(2026, 4, 8), salesReport.getEndDate());
        assertEquals(1, salesReport.getReversalTransactionCount());
        assertEquals(0, salesReport.getTotalReversals().compareTo(BigDecimal.valueOf(75.00)));
        assertEquals(0, salesReport.getNetAmount().compareTo(BigDecimal.valueOf(825.00)));
        assertEquals(1, salesReport.getTransactionHistory().size());
    }

    @Test
    void getIncomeTransactionCount() {
        assertEquals(3, salesReport.getIncomeTransactionCount());
    }

    @Test
    void getTotalIncome() {
        assertEquals(0, salesReport.getTotalIncome().compareTo(BigDecimal.valueOf(900.00)));
    }

    @Test
    void getTransactionHistory() {
        assertEquals(1, salesReport.getTransactionHistory().size());
        assertEquals(401, salesReport.getTransactionHistory().getFirst().getOrderId());
    }
}

