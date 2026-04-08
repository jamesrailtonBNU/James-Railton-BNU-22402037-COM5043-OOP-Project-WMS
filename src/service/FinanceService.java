package service;
// This is used for recording financial transactions related to orders, generating sales and expense reports, and managing account balance. It supports recording income and expenses, reversing transactions, and generating reports based on different time periods (daily, weekly, quarterly, annual)
import model.ExpenseReport;
import model.FinancialTransaction;
import model.ReportType;
import model.SalesReport;
import model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FinanceService {
    private final List<FinancialTransaction> transactions;
    private BigDecimal accountBalance;
    private int nextTransactionId;
    private int nextReportId;

    public FinanceService() {
        this.transactions = new ArrayList<>();
        this.accountBalance = BigDecimal.ZERO;
        this.nextTransactionId = 1;
        this.nextReportId = 1;
    }

    public FinancialTransaction recordIncome(int orderId, BigDecimal amount) {
        return recordTransaction(orderId, amount, TransactionType.INCOME);
    }

    public FinancialTransaction recordExpense(int orderId, BigDecimal amount) {
        return recordTransaction(orderId, amount, TransactionType.EXPENSE);
    }

    public FinancialTransaction recordTransaction(int orderId, BigDecimal amount, TransactionType transactionType) {
        if (orderId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || transactionType == null) {
            return null;
        }

        if (transactionType == TransactionType.REVERSAL) {
            return null;
        }

        FinancialTransaction latestTransaction = getLatestTransactionByOrderId(orderId);
        if (latestTransaction != null
                && latestTransaction.getTransactionType() == TransactionType.REVERSAL
                && latestTransaction.isActive()) {
            if (!latestTransaction.voidTransaction()) {
                return null;
            }

            accountBalance = accountBalance.subtract(getSignedEffect(latestTransaction));
        }

        FinancialTransaction transaction = new FinancialTransaction(
                nextTransactionId++,
                orderId,
                LocalDateTime.now(),
                amount,
                transactionType
        );

        transactions.add(transaction);
        accountBalance = accountBalance.add(getSignedEffect(transaction));
        return transaction;
    }

    FinancialTransaction reverseOrderTransaction(int orderId) {
        if (orderId <= 0) {
            return null;
        }

        FinancialTransaction latestTransaction = getLatestTransactionByOrderId(orderId);
        if (latestTransaction == null || !latestTransaction.isActive()) {
            return null;
        }

        if (latestTransaction.getTransactionType() == TransactionType.REVERSAL) {
            return null;
        }

        FinancialTransaction originalTransaction = getLatestNonReversalTransactionByOrderId(orderId);
        if (originalTransaction == null) {
            return null;
        }

        BigDecimal reversalEffect = getReversalEffect(originalTransaction);

        FinancialTransaction reversalTransaction = new FinancialTransaction(
                nextTransactionId++,
                orderId,
                LocalDateTime.now(),
                originalTransaction.getTransactionAmount(),
                TransactionType.REVERSAL
        );

        transactions.add(reversalTransaction);
        accountBalance = accountBalance.add(reversalEffect);
        return reversalTransaction;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public List<FinancialTransaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<FinancialTransaction> getRecentTransactions() {
        List<FinancialTransaction> recentTransactions = new ArrayList<>();
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);

        for (FinancialTransaction transaction : transactions) {
            if (!transaction.getTransactionDateTime().isBefore(cutoff)) {
                recentTransactions.add(transaction);
            }
        }

        return recentTransactions;
    }

    public SalesReport generateSalesReport(ReportType reportType) {
        if (reportType == null) {
            return null;
        }

        LocalDateTime reportGeneratedAt = LocalDateTime.now();
        LocalDateTime startDateTime = determineStartDateTime(reportType, reportGeneratedAt);
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = reportGeneratedAt.toLocalDate();
        List<FinancialTransaction> reportTransactions = getTransactionsForReport(startDateTime, reportGeneratedAt);

        return new SalesReport(
                nextReportId++,
                reportType,
                startDate,
                reportGeneratedAt,
                endDate,
                countTransactionsByType(reportTransactions, TransactionType.REVERSAL),
                sumTransactionAmountsByType(reportTransactions, TransactionType.REVERSAL),
                calculateNetAmount(reportTransactions),
                countTransactionsByType(reportTransactions, TransactionType.INCOME),
                sumTransactionAmountsByType(reportTransactions, TransactionType.INCOME),
                reportTransactions
        );
    }

    public ExpenseReport generateExpenseReport(ReportType reportType) {
        if (reportType == null) {
            return null;
        }

        LocalDateTime reportGeneratedAt = LocalDateTime.now();
        LocalDateTime startDateTime = determineStartDateTime(reportType, reportGeneratedAt);
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = reportGeneratedAt.toLocalDate();
        List<FinancialTransaction> reportTransactions = getTransactionsForReport(startDateTime, reportGeneratedAt);

        return new ExpenseReport(
                nextReportId++,
                reportType,
                startDate,
                reportGeneratedAt,
                endDate,
                countTransactionsByType(reportTransactions, TransactionType.REVERSAL),
                sumTransactionAmountsByType(reportTransactions, TransactionType.REVERSAL),
                calculateNetAmount(reportTransactions),
                countTransactionsByType(reportTransactions, TransactionType.EXPENSE),
                sumTransactionAmountsByType(reportTransactions, TransactionType.EXPENSE),
                reportTransactions
        );
    }

    private LocalDateTime determineStartDateTime(ReportType reportType, LocalDateTime reportGeneratedAt) {
        if (reportType == ReportType.DAILY) {
            return reportGeneratedAt.toLocalDate().atStartOfDay();
        }

        if (reportType == ReportType.WEEKLY) {
            return reportGeneratedAt.minusDays(7);
        }

        if (reportType == ReportType.QUARTERLY) {
            return reportGeneratedAt.minusDays(91);
        }

        if (reportType == ReportType.ANNUAL) {
            return reportGeneratedAt.minusDays(365);
        }

        throw new IllegalStateException("Unexpected report type: " + reportType);
    }

    private List<FinancialTransaction> getTransactionsForReport(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<FinancialTransaction> reportTransactions = new ArrayList<>();

        for (FinancialTransaction transaction : transactions) {
            if (isIncludedInReport(transaction, startDateTime, endDateTime)) {
                reportTransactions.add(transaction);
            }
        }

        return reportTransactions;
    }

    private int countTransactionsByType(List<FinancialTransaction> reportTransactions, TransactionType transactionType) {
        int count = 0;

        for (FinancialTransaction transaction : reportTransactions) {
            if (transaction.getTransactionType() == transactionType) {
                count++;
            }
        }

        return count;
    }

    private BigDecimal sumTransactionAmountsByType(List<FinancialTransaction> reportTransactions, TransactionType transactionType) {
        BigDecimal total = BigDecimal.ZERO;

        for (FinancialTransaction transaction : reportTransactions) {
            if (transaction.getTransactionType() == transactionType) {
                total = total.add(transaction.getTransactionAmount());
            }
        }

        return total;
    }

    private BigDecimal calculateNetAmount(List<FinancialTransaction> reportTransactions) {
        BigDecimal netAmount = BigDecimal.ZERO;

        for (FinancialTransaction transaction : reportTransactions) {
            if (transaction.getTransactionType() == TransactionType.INCOME) {
                netAmount = netAmount.add(transaction.getTransactionAmount());
            } else if (transaction.getTransactionType() == TransactionType.EXPENSE) {
                netAmount = netAmount.subtract(transaction.getTransactionAmount());
            } else if (transaction.getTransactionType() == TransactionType.REVERSAL) {
                netAmount = netAmount.add(getReversalEffect(transaction));
            } else {
                throw new IllegalStateException("Unexpected transaction type: " + transaction.getTransactionType());
            }
        }

        return netAmount;
    }

    private boolean isIncludedInReport(FinancialTransaction transaction, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (!transaction.isActive()) {
            return false;
        }

        LocalDateTime transactionDateTime = transaction.getTransactionDateTime();
        return !transactionDateTime.isBefore(startDateTime) && !transactionDateTime.isAfter(endDateTime);
    }

    private FinancialTransaction getLatestTransactionByOrderId(int orderId) {
        for (int i = transactions.size() - 1; i >= 0; i--) {
            FinancialTransaction transaction = transactions.get(i);
            if (transaction.getOrderId() == orderId) {
                return transaction;
            }
        }
        return null;
    }

    private FinancialTransaction getLatestNonReversalTransactionByOrderId(int orderId) {
        for (int i = transactions.size() - 1; i >= 0; i--) {
            FinancialTransaction transaction = transactions.get(i);
            if (transaction.getOrderId() == orderId && transaction.getTransactionType() != TransactionType.REVERSAL) {
                return transaction;
            }
        }
        return null;
    }

    private BigDecimal getSignedEffect(FinancialTransaction transaction) {
        if (transaction.getTransactionType() == TransactionType.INCOME) {
            return transaction.getTransactionAmount();
        }

        if (transaction.getTransactionType() == TransactionType.EXPENSE) {
            return transaction.getTransactionAmount().negate();
        }

        if (transaction.getTransactionType() == TransactionType.REVERSAL) {
            return getReversalEffect(transaction);
        }

        throw new IllegalStateException("Unexpected transaction type: " + transaction.getTransactionType());
    }

    private BigDecimal getReversalEffect(FinancialTransaction reversalTransaction) {
        FinancialTransaction originalTransaction = getLatestNonReversalTransactionByOrderId(reversalTransaction.getOrderId());

        if (originalTransaction == null) {
            return BigDecimal.ZERO;
        }

        if (originalTransaction.getTransactionType() == TransactionType.INCOME) {
            return reversalTransaction.getTransactionAmount().negate();
        }

        if (originalTransaction.getTransactionType() == TransactionType.EXPENSE) {
            return reversalTransaction.getTransactionAmount();
        }

        if (originalTransaction.getTransactionType() == TransactionType.REVERSAL) {
            return BigDecimal.ZERO;
        }

        throw new IllegalStateException("Unexpected transaction type: " + originalTransaction.getTransactionType());
    }
}