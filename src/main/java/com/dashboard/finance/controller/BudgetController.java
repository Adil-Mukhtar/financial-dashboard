package com.dashboard.finance.controller;

import com.dashboard.finance.dto.BudgetRequest;
import com.dashboard.finance.model.Budget;
import com.dashboard.finance.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "http://localhost:3000")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Budget> createBudget(
            @Valid @RequestBody BudgetRequest request,
            Authentication authentication) {
        try {
            Budget budget = budgetService.createBudget(authentication.getName(), request);
            return ResponseEntity.ok(budget);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<Budget>> getBudgetsByMonth(
            @PathVariable int year,
            @PathVariable int month,
            Authentication authentication) {
        try {
            List<Budget> budgets = budgetService.getUserBudgetsByMonth(
                    authentication.getName(), year, month);
            return ResponseEntity.ok(budgets);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request,
            Authentication authentication) {
        try {
            Budget budget = budgetService.updateBudget(authentication.getName(), id, request);
            return ResponseEntity.ok(budget);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            budgetService.deleteBudget(authentication.getName(), id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}