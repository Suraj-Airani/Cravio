package com.cravio.dao.impl;

import com.cravio.dao.OrderItemDAO;
import com.cravio.model.OrderItem;
import com.cravio.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderItemDAOImpl implements OrderItemDAO {

    // ── SQL Constants ────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO order_items (order_id, menu_item_id, item_name, quantity, price_at_order) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, order_id, menu_item_id, item_name, quantity, price_at_order FROM order_items WHERE id = ?";

    private static final String FIND_BY_ORDER_ID_SQL =
            "SELECT id, order_id, menu_item_id, item_name, quantity, price_at_order FROM order_items WHERE order_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM order_items WHERE id = ?";

    private static final String DELETE_BY_ORDER_ID_SQL =
            "DELETE FROM order_items WHERE order_id = ?";

    // ── CRUD Operations ──────────────────────────────────────────────────

    @Override
    public int insert(OrderItem orderItem) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuItemId());
            ps.setString(3, orderItem.getItemName());
            ps.setInt(4, orderItem.getQuantity());
            ps.setBigDecimal(5, orderItem.getPriceAtOrder());

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
    public void insertBatch(List<OrderItem> orderItems) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);
            try {
                for (OrderItem item : orderItems) {
                    ps.setInt(1, item.getOrderId());
                    ps.setInt(2, item.getMenuItemId());
                    ps.setString(3, item.getItemName());
                    ps.setInt(4, item.getQuantity());
                    ps.setBigDecimal(5, item.getPriceAtOrder());
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public Optional<OrderItem> findById(int id) throws SQLException {
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
    public List<OrderItem> findByOrderId(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ORDER_ID_SQL)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        }
        return items;
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
    public int deleteByOrderId(int orderId) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_ORDER_ID_SQL)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate();
        }
    }

    // ── Row Mapper ───────────────────────────────────────────────────────

    private OrderItem mapRow(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setMenuItemId(rs.getInt("menu_item_id"));
        item.setItemName(rs.getString("item_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPriceAtOrder(rs.getBigDecimal("price_at_order"));
        return item;
    }
}