CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    phone VARCHAR(32) DEFAULT NULL,
    email VARCHAR(128) DEFAULT NULL,
    password_hash VARCHAR(255) DEFAULT NULL,
    role VARCHAR(32) NOT NULL DEFAULT 'user',
    status VARCHAR(32) NOT NULL DEFAULT 'enabled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_users_username (username)
);

CREATE TABLE IF NOT EXISTS engineer_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    real_name VARCHAR(64) NOT NULL,
    skills VARCHAR(500) NOT NULL,
    service_tags VARCHAR(500) NOT NULL,
    intro VARCHAR(1000) DEFAULT NULL,
    verify_status VARCHAR(32) NOT NULL DEFAULT 'pending',
    online_status VARCHAR(32) NOT NULL DEFAULT 'offline',
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    completed_order_count INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_engineer_user_id (user_id),
    KEY idx_engineer_verify_status (verify_status)
);

CREATE TABLE IF NOT EXISTS service_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL,
    user_id BIGINT NOT NULL,
    engineer_id BIGINT NOT NULL,
    service_title VARCHAR(200) NOT NULL,
    requirement_desc VARCHAR(2000) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'pending_dispatch',
    pay_status VARCHAR(32) NOT NULL DEFAULT 'unpaid',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_service_order_order_no (order_no),
    KEY idx_service_order_user_id (user_id),
    KEY idx_service_order_engineer_id (engineer_id)
);

INSERT INTO users (id, username, phone, email, password_hash, role, status)
VALUES (1, 'demo_user', '13800000000', 'demo@gametech.local', 'demo_password_hash', 'user', 'enabled')
ON DUPLICATE KEY UPDATE username = VALUES(username);

INSERT INTO users (id, username, phone, email, password_hash, role, status)
VALUES (11, 'demo_engineer', '13900000000', 'engineer@gametech.local', 'demo_password_hash', 'engineer', 'enabled')
ON DUPLICATE KEY UPDATE username = VALUES(username);

INSERT INTO engineer_profiles (id, user_id, real_name, skills, service_tags, intro, verify_status, online_status, rating, completed_order_count)
VALUES (1001, 11, 'Demo Engineer', 'Windows deployment, dedicated server, optimization', 'game install, bug fix, script customization', 'Can handle remote deployment and environment troubleshooting.', 'approved', 'online', 4.90, 36)
ON DUPLICATE KEY UPDATE real_name = VALUES(real_name);
