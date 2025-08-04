package com.dashboard.finance.model;

import java.math.BigDecimal;

public class Budget {
    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private Integer month;
    private Integer year;

    // Additional fields for analytics
    private String categoryName;
    private String categoryColor;
    private BigDecimal spent;
    private BigDecimal remaining;

    // Constructors
    public Budget() {}

    public Budget(Long userId, Long categoryId, BigDecimal amount, Integer month, Integer year) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    // Additional getters/setters
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryColor() { return categoryColor; }
    public void setCategoryColor(String categoryColor) { this.categoryColor = categoryColor; }

    public BigDecimal getSpent() { return spent; }
    public void setSpent(BigDecimal spent) { this.spent = spent; }

    public BigDecimal getRemaining() { return remaining; }
    public void setRemaining(BigDecimal remaining) { this.remaining = remaining; }
}