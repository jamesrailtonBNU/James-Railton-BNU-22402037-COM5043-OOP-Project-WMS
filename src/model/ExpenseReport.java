package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
// This class represents an expense report, which extends the financial report with specific details about expenses.

public class ExpenseReport extends FinancialReport {
    private final int expenseTransactionCount;
    private final BigDecimal totalExpenses;

    public ExpenseReport(int reportId,
                         ReportType reportType,
                         LocalDate startDate,
                         LocalDateTime reportGeneratedAt,
                         LocalDate endDate,
                         int reversalTransactionCount,
                         BigDecimal totalReversals,
                         BigDecimal netAmount,
                         int expenseTransactionCount,
                         BigDecimal totalExpenses,
                         List<FinancialTransaction> transactionHistory) {
        super(reportId, reportType, startDate, reportGeneratedAt, endDate, reversalTransactionCount, totalReversals, netAmount, transactionHistory);

        this.expenseTransactionCount = expenseTransactionCount;
        this.totalExpenses = totalExpenses;
    }

    public int getExpenseTransactionCount() {
        return expenseTransactionCount;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }
}

