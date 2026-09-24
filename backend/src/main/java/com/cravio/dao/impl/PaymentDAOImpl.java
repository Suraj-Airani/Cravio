package com.cravio.dao.impl;

import com.cravio.dao.PaymentDAO;
import com.cravio.model.Payment;
import com.cravio.model.PaymentStatus;
import com.cravio.util.DBConnectionUtil;

import java.sql.*;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

    // ── SQL Constants ────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO payments (order_id, amount, status) VALUES (?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, order_id, amount, status, created_at, updated_at FROM payments WHERE id = ?";

    private static final String FIND_BY_ORDER_ID_SQL =
            "SELECT id, order_id, amount, status, created_at, updated_at FROM payments WHERE order_id = ?";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE payments SET status = ? WHERE id = ?";

    private static final String UPDATE_SQL =
            "UPDATE payments SET order_id = ?, amount = ?, status = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM payments WHERE id = ?";

    // ── CRUD Operations ──────────────────────────────────────────────────

    @Override
    public int insert(Payment payment) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, payment.getOrderId());
            ps.setBigDecimal(2, payment.getAmount());
            ps.setString(3, payment.getStatus().name());

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
    public Optional<Payment> findById(int id) throws SQLException {
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
    public Optional<Payment> findByOrderId(int orderId) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ORDER_ID_SQL)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean updateStatus(int id, PaymentStatus status) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS_SQL)) {

            ps.setString(1, status.name());
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Payment payment) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, payment.getOrderId());
            ps.setBigDecimal(2, payment.getAmount());
            ps.setString(3, payment.getStatus().name());
            ps.setInt(4, payment.getId());

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

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        payment.setCreatedAt(rs.getTimestamp("created_at"));
        payment.setUpdatedAt(rs.getTimestamp("updated_at"));
        return payment;
    }
}