package com.cravio.dao;

import com.cravio.model.Restaurant;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RestaurantDAO {

    int insert(Restaurant restaurant) throws SQLException;

    Optional<Restaurant> findById(int id) throws SQLException;

    List<Restaurant> findAll() throws SQLException;

    List<Restaurant> findAllActive() throws SQLException;

    List<Restaurant> findByCuisineType(String cuisineType) throws SQLException;

    boolean update(Restaurant restaurant) throws SQLException;

    boolean delete(int id) throws SQLException;
}