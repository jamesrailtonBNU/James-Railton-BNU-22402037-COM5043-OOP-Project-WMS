package model;
// This is used for both daily and monthly financial reports, as they share the same structure. The only difference is the report type and the date range they cover.

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FinancialReport {
    private final int reportId;
    private final ReportType reportType;
    private final LocalDate startDate;
    private final LocalDateTime reportGeneratedAt;
    private final LocalDate endDate;
    private final int reversalTransactionCount;
    private final BigDecimal totalReversals;
    private final BigDecimal netAmount;
    private final List<FinancialTransaction> transactionHistory;

    public FinancialReport(int reportId,
                           ReportType reportType,
                           LocalDate startDate,
                           LocalDateTime reportGeneratedAt,
                           LocalDate endDate,
                           int reversalTransactionCount,
                           BigDecimal totalReversals,
                           BigDecimal netAmount,
                           List<FinancialTransaction> transactionHistory) {

        this.reportId = reportId;
        this.reportType = reportType;
        this.startDate = startDate;
        this.reportGeneratedAt = reportGeneratedAt;
        this.endDate = endDate;
        this.reversalTransactionCount = reversalTransactionCount;
        this.totalReversals = totalReversals;
        this.netAmount = netAmount;
        this.transactionHistory = Collections.unmodifiableList(new ArrayList<>(transactionHistory));
    }

    public int getReportId() {
        return reportId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDateTime getReportGeneratedAt() {
        return reportGeneratedAt;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getReversalTransactionCount() {
        return reversalTransactionCount;
    }

    public BigDecimal getTotalReversals() {
        return totalReversals;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public List<FinancialTransaction> getTransactionHistory() {
        return transactionHistory;
    }
}