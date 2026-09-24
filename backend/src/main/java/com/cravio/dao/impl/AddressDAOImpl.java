package com.cravio.dao.impl;

import com.cravio.dao.AddressDAO;
import com.cravio.model.Address;
import com.cravio.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressDAOImpl implements AddressDAO {

    // ── SQL Constants ────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO addresses (user_id, address_line, city, pincode, is_default) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, user_id, address_line, city, pincode, is_default, created_at FROM addresses WHERE id = ?";

    private static final String FIND_BY_USER_ID_SQL =
            "SELECT id, user_id, address_line, city, pincode, is_default, created_at FROM addresses WHERE user_id = ?";

    private static final String FIND_DEFAULT_BY_USER_ID_SQL =
            "SELECT id, user_id, address_line, city, pincode, is_default, created_at FROM addresses WHERE user_id = ? AND is_default = TRUE";

    private static final String UPDATE_SQL =
            "UPDATE addresses SET address_line = ?, city = ?, pincode = ?, is_default = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM addresses WHERE id = ?";

    // ── CRUD Operations ──────────────────────────────────────────────────

    @Override
    public int insert(Address address) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getAddressLine());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getPincode());
            ps.setBoolean(5, address.isDefault());

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
    public Optional<Address> findById(int id) throws SQLException {
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
    public List<Address> findByUserId(int userId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USER_ID_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapRow(rs));
                }
            }
        }
        return addresses;
    }

    @Override
    public Optional<Address> findDefaultByUserId(int userId) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_DEFAULT_BY_USER_ID_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Address address) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, address.getAddressLine());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getPincode());
            ps.setBoolean(4, address.isDefault());
            ps.setInt(5, address.getId());

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

    private Address mapRow(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setUserId(rs.getInt("user_id"));
        address.setAddressLine(rs.getString("address_line"));
        address.setCity(rs.getString("city"));
        address.setPincode(rs.getString("pincode"));
        address.setDefault(rs.getBoolean("is_default"));
        address.setCreatedAt(rs.getTimestamp("created_at"));
        return address;
    }
}