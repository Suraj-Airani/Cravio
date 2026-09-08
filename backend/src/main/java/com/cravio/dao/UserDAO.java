package com.cravio.dao;

import com.cravio.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {

    int insert(User user) throws SQLException;

    Optional<User> findById(int id) throws SQLException;

    Optional<User> findByEmail(String email) throws SQLException;

    List<User> findAll() throws SQLException;

    boolean update(User user) throws SQLException;

    boolean delete(int id) throws SQLException;
}