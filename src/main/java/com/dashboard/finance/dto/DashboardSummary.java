package com.dashboard.finance.dto;

import com.dashboard.finance.model.Budget;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummary {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal balance;
    private List<MonthlyExpenseSummary> expensesByCategory;
    private List<Budget> budgets;

    public DashboardSummary() {}

    // Getters and Setters
    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public List<MonthlyExpenseSummary> getExpensesByCategory() { return expensesByCategory; }
    public void setExpensesByCategory(List<MonthlyExpenseSummary> expensesByCategory) {
        this.expensesByCategory = expensesByCategory;
    }

    public List<com.dashboard.finance.model.Budget> getBudgets() { return budgets; }
    public void setBudgets(List<com.dashboard.finance.model.Budget> budgets) { this.budgets = budgets; }
}
