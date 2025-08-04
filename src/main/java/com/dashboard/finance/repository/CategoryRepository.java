package com.dashboard.finance.repository;

import com.dashboard.finance.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setType(rs.getString("type"));
        category.setColor(rs.getString("color"));
        return category;
    };

    public List<Category> findAll() {
        String sql = "SELECT * FROM categories ORDER BY name";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    public List<Category> findByType(String type) {
        String sql = "SELECT * FROM categories WHERE type = ? ORDER BY name";
        return jdbcTemplate.query(sql, categoryRowMapper, type);
    }

    public Optional<Category> findById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        List<Category> categories = jdbcTemplate.query(sql, categoryRowMapper, id);
        return categories.isEmpty() ? Optional.empty() : Optional.of(categories.get(0));
    }
}