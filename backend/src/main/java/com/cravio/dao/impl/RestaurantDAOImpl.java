package com.cravio.dao.impl;

import com.cravio.dao.RestaurantDAO;
import com.cravio.model.Restaurant;
import com.cravio.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantDAOImpl implements RestaurantDAO {

    // ── SQL Constants ────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO restaurants (name, cuisine_type, rating, is_active) VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT id, name, cuisine_type, rating, is_active, created_at FROM restaurants WHERE id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT id, name, cuisine_type, rating, is_active, created_at FROM restaurants";

    private static final String FIND_ALL_ACTIVE_SQL =
            "SELECT id, name, cuisine_type, rating, is_active, created_at FROM restaurants WHERE is_active = TRUE";

    private static final String FIND_BY_CUISINE_TYPE_SQL =
            "SELECT id, name, cuisine_type, rating, is_active, created_at FROM restaurants WHERE cuisine_type = ?";

    private static final String UPDATE_SQL =
            "UPDATE restaurants SET name = ?, cuisine_type = ?, rating = ?, is_active = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM restaurants WHERE id = ?";

    // ── CRUD Operations ──────────────────────────────────────────────────

    @Override
    public int insert(Restaurant restaurant) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getCuisineType());
            ps.setBigDecimal(3, restaurant.getRating());
            ps.setBoolean(4, restaurant.isActive());

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
    public Optional<Restaurant> findById(int id) throws SQLException {
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
    public List<Restaurant> findAll() throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                restaurants.add(mapRow(rs));
            }
        }
        return restaurants;
    }

    @Override
    public List<Restaurant> findAllActive() throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_ACTIVE_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                restaurants.add(mapRow(rs));
            }
        }
        return restaurants;
    }

    @Override
    public List<Restaurant> findByCuisineType(String cuisineType) throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_CUISINE_TYPE_SQL)) {

            ps.setString(1, cuisineType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(mapRow(rs));
                }
            }
        }
        return restaurants;
    }

    @Override
    public boolean update(Restaurant restaurant) throws SQLException {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getCuisineType());
            ps.setBigDecimal(3, restaurant.getRating());
            ps.setBoolean(4, restaurant.isActive());
            ps.setInt(5, restaurant.getId());

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

    private Restaurant mapRow(ResultSet rs) throws SQLException {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(rs.getInt("id"));
        restaurant.setName(rs.getString("name"));
        restaurant.setCuisineType(rs.getString("cuisine_type"));
        restaurant.setRating(rs.getBigDecimal("rating"));
        restaurant.setActive(rs.getBoolean("is_active"));
        restaurant.setCreatedAt(rs.getTimestamp("created_at"));
        return restaurant;
    }
}