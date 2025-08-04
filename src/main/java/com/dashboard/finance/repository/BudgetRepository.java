package com.dashboard.finance.repository;

import com.dashboard.finance.model.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BudgetRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Budget> budgetRowMapper = (rs, rowNum) -> {
        Budget budget = new Budget();
        budget.setId(rs.getLong("id"));
        budget.setUserId(rs.getLong("user_id"));
        budget.setCategoryId(rs.getLong("category_id"));
        budget.setAmount(rs.getBigDecimal("amount"));
        budget.setMonth(rs.getInt("month"));
        budget.setYear(rs.getInt("year"));

        // Category info if available
        try {
            budget.setCategoryName(rs.getString("category_name"));
            budget.setCategoryColor(rs.getString("category_color"));
            budget.setSpent(rs.getBigDecimal("spent"));
            budget.setRemaining(rs.getBigDecimal("remaining"));
        } catch (Exception e) {
            // These columns not in this query
        }

        return budget;
    };

    public Budget save(Budget budget) {
        String sql = "INSERT INTO budgets (user_id, category_id, amount, month, year) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, budget.getUserId());
            ps.setLong(2, budget.getCategoryId());
            ps.setBigDecimal(3, budget.getAmount());
            ps.setInt(4, budget.getMonth());
            ps.setInt(5, budget.getYear());
            return ps;
        }, keyHolder);

        budget.setId(keyHolder.getKey().longValue());
        return budget;
    }

    public List<Budget> findByUserIdAndMonth(Long userId, int year, int month) {
        String sql = """
            SELECT b.*, c.name as category_name, c.color as category_color,
                   COALESCE(SUM(t.amount), 0) as spent,
                   (b.amount - COALESCE(SUM(t.amount), 0)) as remaining
            FROM budgets b
            JOIN categories c ON b.category_id = c.id
            LEFT JOIN transactions t ON b.category_id = t.category_id 
                AND t.user_id = b.user_id
                AND EXTRACT(YEAR FROM t.transaction_date) = b.year
                AND EXTRACT(MONTH FROM t.transaction_date) = b.month
                AND c.type = 'EXPENSE'
            WHERE b.user_id = ? AND b.year = ? AND b.month = ?
            GROUP BY b.id, c.name, c.color, b.amount
            ORDER BY c.name
            """;
        return jdbcTemplate.query(sql, budgetRowMapper, userId, year, month);
    }

    public Optional<Budget> findByUserCategoryAndMonth(Long userId, Long categoryId, int year, int month) {
        String sql = "SELECT * FROM budgets WHERE user_id = ? AND category_id = ? AND year = ? AND month = ?";
        List<Budget> budgets = jdbcTemplate.query(sql, budgetRowMapper, userId, categoryId, year, month);
        return budgets.isEmpty() ? Optional.empty() : Optional.of(budgets.get(0));
    }

    public Budget update(Budget budget) {
        String sql = "UPDATE budgets SET amount = ? WHERE id = ?";
        jdbcTemplate.update(sql, budget.getAmount(), budget.getId());
        return budget;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM budgets WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}