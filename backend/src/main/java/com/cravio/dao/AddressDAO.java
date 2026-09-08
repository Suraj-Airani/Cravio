package com.cravio.dao;

import com.cravio.model.Address;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AddressDAO {

    int insert(Address address) throws SQLException;

    Optional<Address> findById(int id) throws SQLException;

    List<Address> findByUserId(int userId) throws SQLException;

    Optional<Address> findDefaultByUserId(int userId) throws SQLException;

    boolean update(Address address) throws SQLException;

    boolean delete(int id) throws SQLException;
}