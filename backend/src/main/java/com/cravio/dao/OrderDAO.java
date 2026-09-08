package com.cravio.dao;

import com.cravio.model.Order;
import com.cravio.model.OrderStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {

    int insert(Order order) throws SQLException;

    Optional<Order> findById(int id) throws SQLException;

    List<Order> findByUserId(int userId) throws SQLException;

    List<Order> findByStatus(OrderStatus status) throws SQLException;

    List<Order> findByRestaurantId(int restaurantId) throws SQLException;

    boolean updateStatus(int id, OrderStatus status) throws SQLException;

    boolean update(Order order) throws SQLException;

    boolean delete(int id) throws SQLException;
}