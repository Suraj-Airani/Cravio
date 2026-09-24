package com.cravio.dao.impl;

import com.cravio.dao.MenuItemDAO;
import com.cravio.model.MenuItem;
import com.cravio.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemDAOImpl implements MenuItemDAO {

    // ── SQL Constants ────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO menu_items (restaurant_id, name, price, is_veg, is_available) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, restaurant_id, name, price, is_veg, is_available, created_at FROM menu_items WHERE id = ?";

    private static final String FIND_BY_RESTAURANT_ID_SQL =
            "SELECT id, restaurant_id, name, price, is_veg, is_available, created_at FROM menu_items WHERE restaurant_id = ?";

    private static final String FIND_AVAILABLE_BY_RESTAURANT_ID_SQL =
            "SELECT id, restaurant_id, name, price, is_veg, is_available, created_at FROM menu_items WHERE restaurant_id = ? AND is_available = TRUE";

    private static final String UPDATE_SQL =
            "UPDATE menu_items SET restaurant_id = ?, name = ?, price = ?, is_veg = ?, is_available = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM menu_items WHERE id = ?";

    // ── CRUD Operations ──────────────────────────────────────────────────

    @Override
    public int insert(MenuItem menuItem) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, menuItem.getRestaurantId());
            ps.setString(2, menuItem.getName());
            ps.setBigDecimal(3, menuItem.getPrice());
            ps.setBoolean(4, menuItem.isVeg());
            ps.setBoolean(5, menuItem.isAvailable());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Insert succeeded but no generated key was returned.");
        }
    }

    @Override
    public Optional<MenuItem> findById(int id) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<MenuItem> findByRestaurantId(int restaurantId) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_RESTAURANT_ID_SQL)) {

            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        }
        return items;
    }

    @Override
    public List<MenuItem> findAvailableByRestaurantId(int restaurantId) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_AVAILABLE_BY_RESTAURANT_ID_SQL)) {

            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        }
        return items;
    }

    @Override
    public boolean update(MenuItem menuItem) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, menuItem.getRestaurantId());
            ps.setString(2, menuItem.getName());
            ps.setBigDecimal(3, menuItem.getPrice());
            ps.setBoolean(4, menuItem.isVeg());
            ps.setBoolean(5, menuItem.isAvailable());
            ps.setInt(6, menuItem.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Row Mapper ───────────────────────────────────────────────────────

    private MenuItem mapRow(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setId(rs.getInt("id"));
        item.setRestaurantId(rs.getInt("restaurant_id"));
        item.setName(rs.getString("name"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setVeg(rs.getBoolean("is_veg"));
        item.setAvailable(rs.getBoolean("is_available"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        return item;
    }
}