package com.cravio.dao.impl;

import com.cravio.dao.OrderDAO;
import com.cravio.model.Order;
import com.cravio.model.OrderStatus;
import com.cravio.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    // ── SQL Constants ────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO orders (user_id, address_id, restaurant_id, total_amount, status) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, user_id, address_id, restaurant_id, total_amount, status, created_at, updated_at FROM orders WHERE id = ?";

    private static final String FIND_BY_USER_ID_SQL =
            "SELECT id, user_id, address_id, restaurant_id, total_amount, status, created_at, updated_at FROM orders WHERE user_id = ? ORDER BY created_at DESC";

    private static final String FIND_BY_STATUS_SQL =
            "SELECT id, user_id, address_id, restaurant_id, total_amount, status, created_at, updated_at FROM orders WHERE status = ? ORDER BY created_at DESC";

    private static final String FIND_BY_RESTAURANT_ID_SQL =
            "SELECT id, user_id, address_id, restaurant_id, total_amount, status, created_at, updated_at FROM orders WHERE restaurant_id = ? ORDER BY created_at DESC";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE orders SET status = ? WHERE id = ?";

    private static final String UPDATE_SQL =
            "UPDATE orders SET user_id = ?, address_id = ?, restaurant_id = ?, total_amount = ?, status = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM orders WHERE id = ?";

    // ── CRUD Operations ──────────────────────────────────────────────────

    @Override
    public int insert(Order order) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getAddressId());
            ps.setInt(3, order.getRestaurantId());
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getStatus().name());

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
    public Optional<Order> findById(int id) throws SQLException {
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
    public List<Order> findByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USER_ID_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapRow(rs));
                }
            }
        }
        return orders;
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_STATUS_SQL)) {

            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapRow(rs));
                }
            }
        }
        return orders;
    }

    @Override
    public List<Order> findByRestaurantId(int restaurantId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_RESTAURANT_ID_SQL)) {

            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapRow(rs));
                }
            }
        }
        return orders;
    }

    @Override
    public boolean updateStatus(int id, OrderStatus status) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS_SQL)) {

            ps.setString(1, status.name());
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Order order) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getAddressId());
            ps.setInt(3, order.getRestaurantId());
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getStatus().name());
            ps.setInt(6, order.getId());

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

    private Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setAddressId(rs.getInt("address_id"));
        order.setRestaurantId(rs.getInt("restaurant_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }
}