package com.cravio.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MenuItem {

    private int id;
    private int restaurantId;
    private String name;
    private BigDecimal price;
    private boolean isVeg;
    private boolean isAvailable;
    private Timestamp createdAt;

    public MenuItem() {
    }

    public MenuItem(int id, int restaurantId, String name, BigDecimal price,
                    boolean isVeg, boolean isAvailable, Timestamp createdAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.isVeg = isVeg;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean isVeg) {
        this.isVeg = isVeg;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}