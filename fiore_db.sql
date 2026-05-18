-- ================================================================
-- Fiore Flower Shop — MySQL Database Setup Script
-- Run this script in MySQL Workbench or via CLI:
--   mysql -u root -p < fiore_db.sql
-- ================================================================

CREATE DATABASE IF NOT EXISTS fiore_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE fiore_db;

-- ── Drop existing tables (in dependency order) ────────────────────
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS bouquets;
DROP TABLE IF EXISTS users;

-- ── users ─────────────────────────────────────────────────────────
CREATE TABLE users (
    id            INT          NOT NULL AUTO_INCREMENT,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(120) NOT NULL UNIQUE,
    phone         VARCHAR(20)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    address       TEXT,
    date_of_birth DATE,
    role          ENUM('admin','user') NOT NULL DEFAULT 'user',
    status        ENUM('pending','approved','rejected') NOT NULL DEFAULT 'pending',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_email  (email),
    INDEX idx_phone  (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── bouquets ──────────────────────────────────────────────────────
CREATE TABLE bouquets (
    id             INT            NOT NULL AUTO_INCREMENT,
    name           VARCHAR(120)   NOT NULL,
    description    TEXT           NOT NULL,
    price          DECIMAL(10,2)  NOT NULL,
    stock_quantity INT            NOT NULL DEFAULT 0,
    category       VARCHAR(60)    NOT NULL,
    occasion       VARCHAR(60),
    image_path     VARCHAR(255)   NOT NULL DEFAULT 'default.jpg',
    featured       TINYINT(1)     NOT NULL DEFAULT 0,
    created_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_category (category),
    INDEX idx_featured (featured),
    FULLTEXT idx_name_ft (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── orders ────────────────────────────────────────────────────────
CREATE TABLE orders (
    id           INT            NOT NULL AUTO_INCREMENT,
    user_id      INT            NOT NULL,
    bouquet_id   INT            NOT NULL,
    quantity     INT            NOT NULL DEFAULT 1,
    total_price  DECIMAL(10,2)  NOT NULL,
    status       ENUM('pending','confirmed','delivered','cancelled') NOT NULL DEFAULT 'pending',
    special_note TEXT,
    ordered_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_user    FOREIGN KEY (user_id)    REFERENCES users    (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_bouquet FOREIGN KEY (bouquet_id) REFERENCES bouquets (id) ON DELETE CASCADE,
    INDEX idx_user_id    (user_id),
    INDEX idx_bouquet_id (bouquet_id),
    INDEX idx_status     (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- SEED DATA
-- ================================================================

-- ── Admin user (password: admin123) ──────────────────────────────
-- BCrypt hash of "admin123" with work-factor 12:
INSERT INTO users (full_name, email, phone, password_hash, address, role, status)
VALUES (
    'Fiore Admin',
    'admin@fiore.com',
    '+9779800000000',
    '$2a$12$lJCiNTtJfwq8GznTkHDpKOmY5LUNdvbnFbnk5Y4rRNVi.wMvzH7iO',
    'Lakeside, Pokhara, Nepal',
    'admin',
    'approved'
);

-- ── Sample users (password: user1234) ────────────────────────────
INSERT INTO users (full_name, email, phone, password_hash, address, date_of_birth, role, status)
VALUES
('Priya Sharma',    'priya@example.com',  '+9779811111111',
 '$2a$12$c0nqE4YzoxOC4FmRlJ5pIuFQTWf6Jz9yfkDFQ3dOmuvFCH7Boo/i',
 'Thamel, Kathmandu', '1998-03-15', 'user', 'approved'),
('Rajesh Thapa',    'rajesh@example.com', '+9779822222222',
 '$2a$12$c0nqE4YzoxOC4FmRlJ5pIuFQTWf6Jz9yfkDFQ3dOmuvFCH7Boo/i',
 'Lakeside, Pokhara', '1995-07-22', 'user', 'approved'),
('Sita Karki',      'sita@example.com',   '+9779833333333',
 '$2a$12$c0nqE4YzoxOC4FmRlJ5pIuFQTWf6Jz9yfkDFQ3dOmuvFCH7Boo/i',
 'Boudha, Kathmandu', '2000-01-10', 'user', 'pending');

-- ── Sample bouquets ───────────────────────────────────────────────
INSERT INTO bouquets (name, description, price, stock_quantity, category, occasion, image_path, featured)
VALUES
('Rose Romance',
 'A breathtaking arrangement of red and pink roses symbolising deep love and passion. Perfect for anniversaries and romantic gestures.',
 2500.00, 15, 'Real Flowers', 'Wedding', 'rose-romance.jpg', 1),

('Eternal Blossoms',
 'Delicate artificial pink blossoms that last forever, making them a timeless gift for any special occasion.',
 1800.00, 20, 'Artificial Flowers', 'Anniversary', 'eternal-blossoms.jpg', 0),

('Spring Garden',
 'A vibrant mixed bouquet bursting with seasonal colours — ideal for birthdays and celebrations.',
 3200.00, 10, 'Mixed Bouquets', 'Birthday', 'spring-garden.jpg', 1),

('Lavender Dreams',
 'Soothing sprigs of lavender arranged beautifully to bring calm and well-wishes to any recipient.',
 2100.00, 18, 'Real Flowers', 'Get Well', 'lavender-dreams.jpg', 0),

('Sunflower Delight',
 'Bright and cheerful sunflowers arranged to bring joy and congratulations on any achievement.',
 2800.00, 12, 'Seasonal', 'Congratulations', 'sunflower-delight.jpg', 0),

('Tulip Elegance',
 'Sophisticated artificial tulips in soft pastel shades, crafted for corporate gifting and professional settings.',
 3500.00, 0, 'Artificial Flowers', 'Corporate', 'tulip-elegance.jpg', 1),

('White Serenity',
 'Pure white roses and lilies combined for a serene, elegant wedding or memorial bouquet.',
 2200.00, 8, 'Real Flowers', 'Wedding', 'white-serenity.jpg', 0),

('Tropical Burst',
 'An exotic mixed arrangement featuring tropical blooms in vivid oranges and greens for a bold statement.',
 4000.00, 5, 'Mixed Bouquets', 'Birthday', 'tropical-burst.jpg', 0);

-- ── Sample orders ─────────────────────────────────────────────────
INSERT INTO orders (user_id, bouquet_id, quantity, total_price, status, special_note)
VALUES
(2, 1, 1, 2500.00, 'delivered', 'Please add a "Happy Anniversary" card.'),
(2, 3, 2, 6400.00, 'confirmed', ''),
(3, 5, 1, 2800.00, 'pending',   'Leave at reception desk.');

-- ================================================================
-- Quick sanity check
-- ================================================================
SELECT 'Setup complete. Tables created and seeded.' AS message;
SELECT 'Admin login: admin@fiore.com / admin123'    AS credentials;
SELECT 'User login:  priya@example.com / user1234'  AS sample_user;
