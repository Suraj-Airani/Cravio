# Cravio — Database Constraints Reference

CREATE DATABASE IF NOT EXISTS Cravio;
USE Cravio;

## 1. `users`

CREATE TABLE users (
    id          INT           AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    email       VARCHAR(255)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone       VARCHAR(20)   DEFAULT NULL,
    created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

---

## 2. `addresses`

CREATE TABLE addresses (
    id           INT           AUTO_INCREMENT PRIMARY KEY,
    user_id      INT           NOT NULL,
    address_line VARCHAR(500)  NOT NULL,
    city         VARCHAR(100)  NOT NULL,
    pincode      VARCHAR(10)   NOT NULL,
    is_default   BOOLEAN       DEFAULT FALSE,
    created_at   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_addresses_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    INDEX idx_addresses_user (user_id)
);

---

## 3. `restaurants`

CREATE TABLE restaurants (
    id           INT           AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255)  NOT NULL,
    cuisine_type VARCHAR(100),
    rating       DECIMAL(2,1)  DEFAULT 0.0,
    is_active    BOOLEAN       DEFAULT TRUE,
    created_at   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_restaurants_cuisine (cuisine_type)
);
---

## 4. `menu_items`

CREATE TABLE menu_items (
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    restaurant_id  INT            NOT NULL,
    name           VARCHAR(255)   NOT NULL,
    price          DECIMAL(10,2)  NOT NULL,
    is_veg         BOOLEAN        DEFAULT TRUE,
    is_available   BOOLEAN        DEFAULT TRUE,
    created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_menuitems_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
        ON DELETE CASCADE,

    INDEX idx_menuitems_restaurant (restaurant_id)
);

---

## 5. `cart_items`

CREATE TABLE cart_items (
    id           INT       AUTO_INCREMENT PRIMARY KEY,
    user_id      INT       NOT NULL,
    menu_item_id INT       NOT NULL,
    quantity     INT       NOT NULL DEFAULT 1,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cart_menuitem
        FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_cart_user_item
        UNIQUE (user_id, menu_item_id),

    INDEX idx_cart_user (user_id)
);

---

## 6. `orders`

CREATE TABLE orders (
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    user_id        INT            NOT NULL,
    address_id     INT            NOT NULL,
    restaurant_id  INT            NOT NULL,
    total_amount   DECIMAL(10,2)  NOT NULL,
    status         ENUM('PENDING','CONFIRMED','PREPARING','OUT_FOR_DELIVERY','DELIVERED','FAILED','CANCELLED')
                                  NOT NULL DEFAULT 'PENDING',
    created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_orders_address
        FOREIGN KEY (address_id) REFERENCES addresses(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_orders_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
        ON DELETE RESTRICT,

    INDEX idx_orders_user (user_id),
    INDEX idx_orders_status (status)
);
---

## 7. `order_items`

CREATE TABLE order_items (
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    order_id       INT            NOT NULL,
    menu_item_id   INT            NOT NULL,
    item_name      VARCHAR(255)   NOT NULL,
    quantity       INT            NOT NULL,
    price_at_order DECIMAL(10,2)  NOT NULL,

    CONSTRAINT fk_orderitems_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_orderitems_menuitem
        FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
        ON DELETE RESTRICT,

    INDEX idx_orderitems_order (order_id)
);

---

## 8. `payments`

CREATE TABLE payments (
    id         INT            AUTO_INCREMENT PRIMARY KEY,
    order_id   INT            NOT NULL UNIQUE,
    amount     DECIMAL(10,2)  NOT NULL,
    status     ENUM('CREATED','SUCCESS','FAILED')
                              NOT NULL DEFAULT 'CREATED',
    created_at TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE,

    INDEX idx_payments_order (order_id)
);
---