package com.cravio.dao.impl;

import com.cravio.dao.CartDAO;
import com.cravio.model.CartItem;
import com.cravio.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartDAOImpl implements CartDAO {

    // ── SQL Constants ────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO cart_items (user_id, menu_item_id, quantity) VALUES (?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, user_id, menu_item_id, quantity, created_at FROM cart_items WHERE id = ?";

    private static final String FIND_BY_USER_AND_MENU_ITEM_SQL =
            "SELECT id, user_id, menu_item_id, quantity, created_at FROM cart_items WHERE user_id = ? AND menu_item_id = ?";

    private static final String FIND_BY_USER_ID_SQL =
            "SELECT id, user_id, menu_item_id, quantity, created_at FROM cart_items WHERE user_id = ?";

    private static final String UPDATE_SQL =
            "UPDATE cart_items SET quantity = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM cart_items WHERE id = ?";

    private static final String DELETE_BY_USER_ID_SQL =
            "DELETE FROM cart_items WHERE user_id = ?";

    // ── CRUD Operations ──────────────────────────────────────────────────

    @Override
    public int insert(CartItem cartItem) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cartItem.getUserId());
            ps.setInt(2, cartItem.getMenuItemId());
            ps.setInt(3, cartItem.getQuantity());

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
    public Optional<CartItem> findById(int id) throws SQLException {
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
    public Optional<CartItem> findByUserIdAndMenuItemId(int userId, int menuItemId) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USER_AND_MENU_ITEM_SQL)) {

            ps.setInt(1, userId);
            ps.setInt(2, menuItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<CartItem> findByUserId(int userId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USER_ID_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        }
        return items;
    }

    @Override
    public boolean update(CartItem cartItem) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, cartItem.getQuantity());
            ps.setInt(2, cartItem.getId());

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

    @Override
    public int deleteByUserId(int userId) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_USER_ID_SQL)) {

            ps.setInt(1, userId);
            return ps.executeUpdate();
        }
    }

    // ── Row Mapper ───────────────────────────────────────────────────────

    private CartItem mapRow(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setMenuItemId(rs.getInt("menu_item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        return item;
    }
}