package com.dashboard.finance.controller;

import com.dashboard.finance.dto.TransactionRequest;
import com.dashboard.finance.model.Transaction;
import com.dashboard.finance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        try {
            Transaction transaction = transactionService.createTransaction(
                    authentication.getName(), request);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getUserTransactions(Authentication authentication) {
        List<Transaction> transactions = transactionService.getUserTransactions(authentication.getName());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<Transaction>> getTransactionsByMonth(
            @PathVariable int year,
            @PathVariable int month,
            Authentication authentication) {
        try {
            List<Transaction> transactions = transactionService.getUserTransactionsByMonth(
                    authentication.getName(), year, month);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/range")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        try {
            List<Transaction> transactions = transactionService.getUserTransactionsByDateRange(
                    authentication.getName(), startDate, endDate);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        try {
            Transaction transaction = transactionService.updateTransaction(
                    authentication.getName(), id, request);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            transactionService.deleteTransaction(authentication.getName(), id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}