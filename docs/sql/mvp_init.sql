CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    phone VARCHAR(20) NULL,
    email VARCHAR(128) NULL,
    password_hash VARCHAR(128) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    avatar VARCHAR(255) NULL,
    real_name VARCHAR(64) NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_users_phone (phone),
    UNIQUE KEY uk_users_email (email),
    KEY idx_users_role_status (role, status)
);

CREATE TABLE IF NOT EXISTS engineer_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    real_name VARCHAR(64) NOT NULL,
    id_card_mask VARCHAR(64) NULL,
    certificates TEXT NULL,
    skills VARCHAR(255) NOT NULL,
    service_tags VARCHAR(255) NOT NULL,
    intro VARCHAR(500) NULL,
    hourly_price DECIMAL(10, 2) NULL DEFAULT 0.00,
    verification_status VARCHAR(20) NOT NULL DEFAULT 'pending',
    verification_remark VARCHAR(255) NULL,
    online_status VARCHAR(20) NOT NULL DEFAULT 'offline',
    rating DECIMAL(3, 2) NOT NULL DEFAULT 0.00,
    completed_order_count INT NOT NULL DEFAULT 0,
    reviewed_by BIGINT NULL,
    reviewed_at DATETIME NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_engineer_profiles_user_id (user_id),
    KEY idx_engineer_profiles_status (verification_status, online_status)
);

CREATE TABLE IF NOT EXISTS resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(128) NOT NULL,
    category_name VARCHAR(64) NOT NULL,
    description VARCHAR(1000) NULL,
    cover_url VARCHAR(255) NULL,
    file_url VARCHAR(255) NULL,
    file_hash VARCHAR(128) NULL,
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    is_free TINYINT(1) NOT NULL DEFAULT 1,
    status VARCHAR(20) NOT NULL DEFAULT 'published',
    download_count INT NOT NULL DEFAULT 0,
    uploader_id BIGINT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_resources_status_category (status, category_name)
);

CREATE TABLE IF NOT EXISTS resource_download_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resource_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    download_ip VARCHAR(64) NULL,
    created_at DATETIME NOT NULL,
    KEY idx_resource_download_logs_resource (resource_id),
    KEY idx_resource_download_logs_user (user_id)
);

CREATE TABLE IF NOT EXISTS service_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL,
    user_id BIGINT NOT NULL,
    engineer_id BIGINT NULL,
    dispatcher_admin_id BIGINT NULL,
    service_title VARCHAR(128) NOT NULL,
    requirement_desc VARCHAR(2000) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'pending_dispatch',
    pay_status VARCHAR(20) NOT NULL DEFAULT 'unpaid',
    dispatch_remark VARCHAR(255) NULL,
    delivery_note VARCHAR(500) NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    paid_at DATETIME NULL,
    completed_at DATETIME NULL,
    UNIQUE KEY uk_service_orders_order_no (order_no),
    KEY idx_service_orders_user (user_id, status),
    KEY idx_service_orders_engineer (engineer_id, status),
    KEY idx_service_orders_dispatch (status, pay_status)
);

INSERT INTO users (id, username, phone, email, password_hash, role, status, avatar, real_name, created_at, updated_at)
VALUES
    (1, 'demo_user', '13800000000', 'demo_user@example.com', '123456', 'user', 'active', '', 'Demo User', NOW(), NOW()),
    (2, 'engineer_amy', '13800000001', 'engineer_amy@example.com', '123456', 'engineer', 'active', '', 'Amy Chen', NOW(), NOW()),
    (3, 'platform_admin', '13800000002', 'admin@example.com', '123456', 'admin', 'active', '', 'Admin', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

INSERT INTO engineer_profiles (id, user_id, real_name, skills, service_tags, intro, hourly_price, verification_status, online_status, rating, completed_order_count, created_at, updated_at)
VALUES
    (1, 2, 'Amy Chen', 'Unity, C#, Linux', '游戏安装, 性能优化', 'Focused on deployment and troubleshooting for indie game teams.', 199.00, 'approved', 'online', 4.80, 27, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

INSERT INTO resources (id, title, category_name, description, cover_url, file_url, file_hash, price, is_free, status, download_count, uploader_id, created_at, updated_at)
VALUES
    (1, 'Windows Game Deployment Package', 'Deployment', 'Deployment docs and startup scripts for Windows servers.', '', '', 'sha256-demo-1', 19.90, 0, 'published', 128, 3, NOW(), NOW()),
    (2, 'Server Environment Checklist', 'Ops', 'Common pre-launch environment inspection checklist.', '', '', 'sha256-demo-2', 0.00, 1, 'published', 206, 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);
