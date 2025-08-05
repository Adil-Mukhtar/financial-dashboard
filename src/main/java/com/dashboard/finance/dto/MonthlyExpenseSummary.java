package com.dashboard.finance.dto;

import java.math.BigDecimal;

public class MonthlyExpenseSummary {
    private String categoryName;
    private String categoryColor;
    private BigDecimal totalAmount;
    private Long transactionCount;

    public MonthlyExpenseSummary() {}

    public MonthlyExpenseSummary(String categoryName, String categoryColor,
                                 BigDecimal totalAmount, Long transactionCount) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
    }

    // Getters and Setters
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryColor() { return categoryColor; }
    public void setCategoryColor(String categoryColor) { this.categoryColor = categoryColor; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Long getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Long transactionCount) { this.transactionCount = transactionCount; }
}