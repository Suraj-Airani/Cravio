package com.cravio.dao;

import com.cravio.model.OrderItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderItemDAO {

    int insert(OrderItem orderItem) throws SQLException;

    void insertBatch(List<OrderItem> orderItems) throws SQLException;

    Optional<OrderItem> findById(int id) throws SQLException;

    List<OrderItem> findByOrderId(int orderId) throws SQLException;

    boolean delete(int id) throws SQLException;

    int deleteByOrderId(int orderId) throws SQLException;
}