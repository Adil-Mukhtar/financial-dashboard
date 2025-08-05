package com.dashboard.finance.service;

import com.dashboard.finance.dto.DashboardSummary;
import com.dashboard.finance.dto.MonthlyExpenseSummary;
import com.dashboard.finance.model.Budget;
import com.dashboard.finance.model.Transaction;
import com.dashboard.finance.model.User;
import com.dashboard.finance.repository.BudgetRepository;
import com.dashboard.finance.repository.TransactionRepository;
import com.dashboard.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardSummary getDashboardSummary(String username, int year, int month) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get transactions for the month
        List<Transaction> transactions = transactionRepository.findByUserIdAndMonth(
                user.getId(), year, month);

        DashboardSummary summary = new DashboardSummary();

        // Calculate totals
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getCategoryType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getCategoryType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.setTotalIncome(totalIncome);
        summary.setTotalExpenses(totalExpenses);
        summary.setBalance(totalIncome.subtract(totalExpenses));

        // Group expenses by category
        Map<String, List<Transaction>> expensesByCategory = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getCategoryType()))
                .collect(Collectors.groupingBy(Transaction::getCategoryName));

        List<MonthlyExpenseSummary> categoryExpenses = expensesByCategory.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    List<Transaction> categoryTransactions = entry.getValue();

                    BigDecimal total = categoryTransactions.stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    String color = categoryTransactions.get(0).getCategoryColor();

                    return new MonthlyExpenseSummary(categoryName, color, total, (long) categoryTransactions.size());
                })
                .collect(Collectors.toList());

        summary.setExpensesByCategory(categoryExpenses);

        // Get budgets for the month
        List<Budget> budgets = budgetRepository.findByUserIdAndMonth(user.getId(), year, month);
        summary.setBudgets(budgets);

        return summary;
    }
}