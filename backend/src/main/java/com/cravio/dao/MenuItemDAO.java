package com.cravio.dao;

import com.cravio.model.MenuItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MenuItemDAO {

    int insert(MenuItem menuItem) throws SQLException;

    Optional<MenuItem> findById(int id) throws SQLException;

    List<MenuItem> findByRestaurantId(int restaurantId) throws SQLException;

    List<MenuItem> findAvailableByRestaurantId(int restaurantId) throws SQLException;

    boolean update(MenuItem menuItem) throws SQLException;

    boolean delete(int id) throws SQLException;
}