package com.cravio.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Restaurant {

    private int id;
    private String name;
    private String cuisineType;
    private BigDecimal rating;
    private boolean isActive;
    private Timestamp createdAt;

    public Restaurant() {
    }

    public Restaurant(int id, String name, String cuisineType,
                      BigDecimal rating, boolean isActive, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}