package com.dashboard.finance.repository;

import com.dashboard.finance.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for Transaction with Category info
    private final RowMapper<Transaction> transactionRowMapper = (rs, rowNum) -> {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("id"));
        transaction.setUserId(rs.getLong("user_id"));
        transaction.setCategoryId(rs.getLong("category_id"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setDescription(rs.getString("description"));
        transaction.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
        transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Category info if available in query
        try {
            transaction.setCategoryName(rs.getString("category_name"));
            transaction.setCategoryType(rs.getString("category_type"));
            transaction.setCategoryColor(rs.getString("category_color"));
        } catch (Exception e) {
            // Category columns not in this query - that's fine
        }

        return transaction;
    };

    public Transaction save(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, category_id, amount, description, transaction_date) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, transaction.getUserId());
            ps.setLong(2, transaction.getCategoryId());
            ps.setBigDecimal(3, transaction.getAmount());
            ps.setString(4, transaction.getDescription());
            ps.setDate(5, Date.valueOf(transaction.getTransactionDate()));
            return ps;
        }, keyHolder);

        transaction.setId(keyHolder.getKey().longValue());
        return transaction;
    }

    public List<Transaction> findByUserId(Long userId) {
        String sql = """
            SELECT t.*, c.name as category_name, c.type as category_type, c.color as category_color
            FROM transactions t 
            JOIN categories c ON t.category_id = c.id 
            WHERE t.user_id = ? 
            ORDER BY t.transaction_date DESC
            """;
        return jdbcTemplate.query(sql, transactionRowMapper, userId);
    }

    public List<Transaction> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT t.*, c.name as category_name, c.type as category_type, c.color as category_color
            FROM transactions t 
            JOIN categories c ON t.category_id = c.id 
            WHERE t.user_id = ? AND t.transaction_date BETWEEN ? AND ?
            ORDER BY t.transaction_date DESC
            """;
        return jdbcTemplate.query(sql, transactionRowMapper, userId, startDate, endDate);
    }

    public List<Transaction> findByUserIdAndMonth(Long userId, int year, int month) {
        String sql = """
            SELECT t.*, c.name as category_name, c.type as category_type, c.color as category_color
            FROM transactions t 
            JOIN categories c ON t.category_id = c.id 
            WHERE t.user_id = ? 
            AND EXTRACT(YEAR FROM t.transaction_date) = ? 
            AND EXTRACT(MONTH FROM t.transaction_date) = ?
            ORDER BY t.transaction_date DESC
            """;
        return jdbcTemplate.query(sql, transactionRowMapper, userId, year, month);
    }

    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        List<Transaction> transactions = jdbcTemplate.query(sql, transactionRowMapper, id);
        return transactions.isEmpty() ? Optional.empty() : Optional.of(transactions.get(0));
    }

    public void delete(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Transaction update(Transaction transaction) {
        String sql = "UPDATE transactions SET category_id = ?, amount = ?, description = ?, transaction_date = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                transaction.getCategoryId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getId()
        );
        return transaction;
    }
}