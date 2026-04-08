package model;
// This is used for both sales and reversal reports, as they share many common attributes.

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SalesReport extends FinancialReport {
    private final int incomeTransactionCount;
    private final BigDecimal totalIncome;

    public SalesReport(int reportId,
                       ReportType reportType,
                       LocalDate startDate,
                       LocalDateTime reportGeneratedAt,
                       LocalDate endDate,
                       int reversalTransactionCount,
                       BigDecimal totalReversals,
                       BigDecimal netAmount,
                       int incomeTransactionCount,
                       BigDecimal totalIncome,
                       List<FinancialTransaction> transactionHistory) {
        super(reportId, reportType, startDate, reportGeneratedAt, endDate, reversalTransactionCount, totalReversals, netAmount, transactionHistory);



        this.incomeTransactionCount = incomeTransactionCount;
        this.totalIncome = totalIncome;
    }

    public int getIncomeTransactionCount() {
        return incomeTransactionCount;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }
}

