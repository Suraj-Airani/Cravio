package com.cravio.model;

import java.sql.Timestamp;

public class CartItem {

    private int id;
    private int userId;
    private int menuItemId;
    private int quantity;
    private Timestamp createdAt;

    public CartItem() {
    }

    public CartItem(int id, int userId, int menuItemId, int quantity,
                    Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}