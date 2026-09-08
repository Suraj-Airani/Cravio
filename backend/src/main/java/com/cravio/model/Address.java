package com.cravio.model;

import java.sql.Timestamp;

public class Address {

    private int id;
    private int userId;
    private String addressLine;
    private String city;
    private String pincode;
    private boolean isDefault;
    private Timestamp createdAt;

    public Address() {
    }

    public Address(int id, int userId, String addressLine, String city,
                   String pincode, boolean isDefault, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.pincode = pincode;
        this.isDefault = isDefault;
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

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}