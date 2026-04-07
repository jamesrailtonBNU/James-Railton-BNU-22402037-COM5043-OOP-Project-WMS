package model;
import java.math.BigDecimal;
import java.time.LocalDate;

 class FinancialReport {
     private int reportId;
     private ReportType reportType;
     private LocalDate startDate;
     private LocalDate endDate;
     private int transactionCount;
     private BigDecimal totalSales;
     private BigDecimal totalExpenses;
     private BigDecimal netAmount;

     FinancialReport(int reportId, String reportType, LocalDate startDate, LocalDate endDate, int transactionCount, BigDecimal totalSales, BigDecimal totalExpenses) {
         this.reportId = reportId;
         this.reportType = ReportType.DAILY;
         this.startDate = startDate;
         this.endDate = endDate;
         this.transactionCount = transactionCount;
         this.totalSales = totalSales;
         this.totalExpenses = totalExpenses;
         this.netAmount = totalSales.subtract(totalExpenses);
     }
 }