-- ============================================================
-- BloomCart Flower Shop — Database Schema
-- MySQL 8.x
-- ============================================================

CREATE DATABASE IF NOT EXISTS bloomcart_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bloomcart_db;

-- ============================================================
-- TABLE: roles
-- ============================================================
CREATE TABLE roles (
    id      INT          NOT NULL AUTO_INCREMENT,
    name    VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_role_name (name)
) ENGINE=InnoDB;

INSERT INTO roles (name) VALUES ('ADMIN'), ('CUSTOMER');

-- ============================================================
-- TABLE: users
-- ============================================================
CREATE TABLE users (
    id           INT           NOT NULL AUTO_INCREMENT,
    full_name    VARCHAR(100)  NOT NULL,
    email        VARCHAR(150)  NOT NULL,
    phone        VARCHAR(20)   NOT NULL,
    password     VARCHAR(256)  NOT NULL,
    salt         VARCHAR(64)   NOT NULL,
    address      TEXT,
    date_of_birth DATE,
    role_id      INT           NOT NULL DEFAULT 2,
    is_active    TINYINT(1)    NOT NULL DEFAULT 1,
    created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_email (email),
    UNIQUE KEY uq_phone (phone),
    KEY idx_role (role_id),
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: categories
-- ============================================================
CREATE TABLE categories (
    id          INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(80)  NOT NULL,
    description TEXT,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_cat_name (name)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: occasions
-- ============================================================
CREATE TABLE occasions (
    id          INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(80)  NOT NULL,
    description TEXT,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_occ_name (name)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: flowers
-- ============================================================
CREATE TABLE flowers (
    id           INT            NOT NULL AUTO_INCREMENT,
    name         VARCHAR(120)   NOT NULL,
    description  TEXT,
    price        DECIMAL(10,2)  NOT NULL,
    stock_qty    INT            NOT NULL DEFAULT 0,
    category_id  INT,
    occasion_id  INT,
    image_name   VARCHAR(200)   DEFAULT 'default.jpg',
    is_featured  TINYINT(1)     NOT NULL DEFAULT 0,
    is_active    TINYINT(1)     NOT NULL DEFAULT 1,
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_flower_cat (category_id),
    KEY idx_flower_occ (occasion_id),
    CONSTRAINT fk_flower_cat FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_flower_occ FOREIGN KEY (occasion_id) REFERENCES occasions(id)  ON DELETE SET NULL
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: orders
-- ============================================================
CREATE TABLE orders (
    id              INT            NOT NULL AUTO_INCREMENT,
    user_id         INT            NOT NULL,
    total_amount    DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    status          ENUM('PENDING','CONFIRMED','PROCESSING','SHIPPED','DELIVERED','CANCELLED')
                                   NOT NULL DEFAULT 'PENDING',
    delivery_address TEXT          NOT NULL,
    notes           TEXT,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_order_user (user_id),
    KEY idx_order_status (status),
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: order_items
-- ============================================================
CREATE TABLE order_items (
    id          INT            NOT NULL AUTO_INCREMENT,
    order_id    INT            NOT NULL,
    flower_id   INT            NOT NULL,
    quantity    INT            NOT NULL,
    unit_price  DECIMAL(10,2)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_oi_order  (order_id),
    KEY idx_oi_flower (flower_id),
    CONSTRAINT fk_oi_order  FOREIGN KEY (order_id)  REFERENCES orders(id)  ON DELETE CASCADE,
    CONSTRAINT fk_oi_flower FOREIGN KEY (flower_id) REFERENCES flowers(id)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: wishlist
-- ============================================================
CREATE TABLE wishlist (
    id         INT      NOT NULL AUTO_INCREMENT,
    user_id    INT      NOT NULL,
    flower_id  INT      NOT NULL,
    added_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_wish (user_id, flower_id),
    KEY idx_wish_user   (user_id),
    KEY idx_wish_flower (flower_id),
    CONSTRAINT fk_wish_user   FOREIGN KEY (user_id)   REFERENCES users(id)   ON DELETE CASCADE,
    CONSTRAINT fk_wish_flower FOREIGN KEY (flower_id) REFERENCES flowers(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: contact_messages
-- ============================================================
CREATE TABLE contact_messages (
    id         INT          NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL,
    subject    VARCHAR(200),
    message    TEXT         NOT NULL,
    is_read    TINYINT(1)   NOT NULL DEFAULT 0,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_msg_read (is_read)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: activity_logs
-- ============================================================
CREATE TABLE activity_logs (
    id          INT          NOT NULL AUTO_INCREMENT,
    user_id     INT,
    action      VARCHAR(100) NOT NULL,
    description TEXT,
    ip_address  VARCHAR(45),
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_log_user (user_id),
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ============================================================
-- SAMPLE DATA
-- ============================================================

-- Admin user  (password: Admin@1234  — SHA-256 + salt stored as hex)
-- salt = 'bloomsalt01'
-- hash = SHA2(CONCAT('Admin@1234','bloomsalt01'), 256)
INSERT INTO users (full_name, email, phone, password, salt, address, role_id, is_active)
VALUES (
    'BloomCart Admin',
    'admin@bloomcart.com',
    '9800000001',
    SHA2(CONCAT('Admin@1234','bloomsalt01'), 256),
    'bloomsalt01',
    'Kathmandu, Nepal',
    1,
    1
);

-- Customer users
-- password: Customer@123  salt: customersalt01
INSERT INTO users (full_name, email, phone, password, salt, address, date_of_birth, role_id, is_active)
VALUES
(
    'Sara Thapa',
    'sara@example.com',
    '9811111111',
    SHA2(CONCAT('Customer@123','customersalt01'), 256),
    'customersalt01',
    'Lalitpur, Nepal',
    '1998-05-14',
    2,
    1
),
(
    'Rajan Karki',
    'rajan@example.com',
    '9822222222',
    SHA2(CONCAT('Customer@123','customersalt02'), 256),
    'customersalt02',
    'Bhaktapur, Nepal',
    '1995-09-21',
    2,
    1
);

-- Categories
INSERT INTO categories (name, description) VALUES
('Real Flowers',      'Fresh, natural flowers sourced locally and internationally.'),
('Artificial Flowers','Long-lasting synthetic floral arrangements.'),
('Mixed Bouquets',    'Creative combinations of real and artificial flowers.'),
('Seasonal Specials', 'Limited-edition arrangements for seasonal events.');

-- Occasions
INSERT INTO occasions (name, description) VALUES
('Wedding',     'Elegant floral arrangements perfect for weddings.'),
('Anniversary', 'Romantic arrangements to celebrate milestones.'),
('Birthday',    'Bright and cheerful flowers for birthday celebrations.'),
('Get Well',    'Soothing arrangements to lift spirits and aid recovery.'),
('Funeral',     'Dignified floral tributes to honour loved ones.'),
('Valentine',   'Passionate red and pink arrangements for Valentine\'s Day.');

-- Flowers
INSERT INTO flowers (name, description, price, stock_qty, category_id, occasion_id, image_name, is_featured) VALUES
('Rose Romance',
 'A lush arrangement of red and pink roses symbolising deep love.',
 2500.00, 30, 1, 1, 'rose-romance.jpg', 1),

('Eternal Blossoms',
 'A timeless bouquet of white artificial flowers for lasting elegance.',
 1800.00, 50, 2, 2, 'eternal-blossoms.jpg', 1),

('Spring Garden',
 'A vibrant mix of tulips, daisies, and carnations bursting with colour.',
 3200.00, 20, 3, 3, 'spring-garden.jpg', 1),

('Lavender Dreams',
 'Fragrant lavender stems arranged in a serene, calming bouquet.',
 2100.00, 15, 1, 4, 'lavender-dreams.jpg', 1),

('Golden Sunshine',
 'Cheerful sunflowers and yellow roses for a warm, happy feeling.',
 1950.00, 25, 1, 3, 'golden-sunshine.jpg', 0),

('Royal Purple',
 'Deep purple orchids and irises crafted for grand occasions.',
 4500.00, 10, 3, 1, 'royal-purple.jpg', 0),

('White Serenity',
 'Pure white lilies and baby\'s breath for peaceful remembrance.',
 2800.00, 18, 1, 5, 'white-serenity.jpg', 0),

('Valentine Heart',
 'Heart-shaped red rose arrangement — the ultimate Valentine gift.',
 3500.00, 12, 1, 6, 'valentine-heart.jpg', 0),

('Pastel Dreams',
 'Soft pastel gerberas and roses in an artificial mixed arrangement.',
 1600.00, 40, 2, 2, 'pastel-dreams.jpg', 0),

('Tropical Burst',
 'Exotic tropical flowers including birds of paradise and heliconias.',
 5200.00, 8, 3, 3, 'tropical-burst.jpg', 0);
