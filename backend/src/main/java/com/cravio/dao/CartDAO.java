package com.cravio.dao;

import com.cravio.model.CartItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CartDAO {

    int insert(CartItem cartItem) throws SQLException;

    Optional<CartItem> findById(int id) throws SQLException;

    Optional<CartItem> findByUserIdAndMenuItemId(int userId, int menuItemId) throws SQLException;

    List<CartItem> findByUserId(int userId) throws SQLException;

    boolean update(CartItem cartItem) throws SQLException;

    boolean delete(int id) throws SQLException;

    int deleteByUserId(int userId) throws SQLException;
}