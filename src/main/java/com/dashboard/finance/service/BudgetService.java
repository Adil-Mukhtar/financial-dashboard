package com.dashboard.finance.service;

import com.dashboard.finance.dto.BudgetRequest;
import com.dashboard.finance.model.Budget;
import com.dashboard.finance.model.Category;
import com.dashboard.finance.model.User;
import com.dashboard.finance.repository.BudgetRepository;
import com.dashboard.finance.repository.CategoryRepository;
import com.dashboard.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public Budget createBudget(String username, BudgetRequest request) {
        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate category exists and is an expense category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!"EXPENSE".equals(category.getType())) {
            throw new RuntimeException("Budget can only be set for expense categories");
        }

        // Check if budget already exists for this category and month
        if (budgetRepository.findByUserCategoryAndMonth(
                user.getId(), request.getCategoryId(), request.getYear(), request.getMonth()).isPresent()) {
            throw new RuntimeException("Budget already exists for this category and month");
        }

        // Create budget
        Budget budget = new Budget();
        budget.setUserId(user.getId());
        budget.setCategoryId(request.getCategoryId());
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        return budgetRepository.save(budget);
    }

    public List<Budget> getUserBudgetsByMonth(String username, int year, int month) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return budgetRepository.findByUserIdAndMonth(user.getId(), year, month);
    }

    public Budget updateBudget(String username, Long budgetId, BudgetRequest request) {
        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find existing budget and verify ownership
        Budget existingBudget = budgetRepository.findByUserCategoryAndMonth(
                        user.getId(), request.getCategoryId(), request.getYear(), request.getMonth())
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        // Update budget amount
        existingBudget.setAmount(request.getAmount());

        return budgetRepository.update(existingBudget);
    }

    public void deleteBudget(String username, Long budgetId) {
        // Note: We'll need to modify this method to properly validate ownership
        // For now, keeping it simple
        budgetRepository.delete(budgetId);
    }
}