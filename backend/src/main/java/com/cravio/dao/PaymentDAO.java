package com.cravio.dao;

import com.cravio.model.Payment;
import com.cravio.model.PaymentStatus;

import java.sql.SQLException;
import java.util.Optional;

public interface PaymentDAO {

    int insert(Payment payment) throws SQLException;

    Optional<Payment> findById(int id) throws SQLException;

    Optional<Payment> findByOrderId(int orderId) throws SQLException;

    boolean updateStatus(int id, PaymentStatus status) throws SQLException;

    boolean update(Payment payment) throws SQLException;

    boolean delete(int id) throws SQLException;
}