CREATE TABLE system_variables (
    variable_key VARCHAR(255) PRIMARY KEY,
    display_name VARCHAR(255)
);
CREATE TABLE `page_entity` (
        `page_id` varchar(255) NOT NULL,
            `page_access_token` text,
                `user_id` varchar(255) DEFAULT NULL,
                    `status` int DEFAULT NULL,
                        `expires_at` bigint DEFAULT NULL,
                            PRIMARY KEY (`page_id`)
                            );

INSERT INTO `page_entity` (`page_id`, `page_access_token`, `user_id`, `status`, `expires_at`) VALUES ('102261526228668', 'EAAS3HcB5Fc4BOwnztpDmggkWdCgD2DrfDwatewc519gvIs9YYWyZBgONpYNDJPjwsZCZBZBkLxZBReSSkNqioGiCPQs3xbVZASDTgZCPB2F0kZAT6yR8LsdXnJCysyATpITnxFvbOZCkHZBWoGTAU30DwUABcv8DpY5INXlme2uy9ZAzaAI0IByZBFjcBHFPQRV2wl10Gout6f14bHnfuc8ZD', '2000217763729488', 1, NULL);

INSERT INTO `page_entity` (`page_id`, `page_access_token`, `user_id`, `status`, `expires_at`) VALUES ('313176735202296', 'EAAS3HcB5Fc4BO8PDRtouf1Kti7u6iH9aEjjZBHk7tX098nxiWpOWssED1TiR5dMVRAO6RVyPcU1qOMhZBd3jjObWlG6wxZA8zBoEwca9kQ39fCGqpmh8UkEeq6PWOqMLHna3fAQDaEQWqOYIxalMlLPEnjMcU7an7Fx7ntR0IQJhmWP0ZCfhm5qjBcR1AU92Np9cP3hhdUNj', '2000217763729488', 1, NULL);

INSERT INTO `page_entity` (`page_id`, `page_access_token`, `user_id`, `status`, `expires_at`) VALUES ('406454305878420', 'EAAS3HcB5Fc4BO22UGIoCfpRPFghCjeoHNPqZCU7c6eqbzwSZAbpV0VJguZCLkGCZAyZAHFhauv3eUOLsvRcK95STBfZBQ0ZBnTeweXMf6IcufP8jOgYMto4wysmAEAVIqMGPcWdLQCTzP665vEhPEh9ICtKP5f5MDClzweiqErko2BuiwdVl5o3EGrU3QyB1D3lN7ZCLxYhh', '2000217763729488', 1, NULL);


CREATE TABLE page_templates (
    page_id VARCHAR(255) PRIMARY KEY,
    template_content TEXT
);

CREATE TABLE sender (
    sender_id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    avatar TEXT
);

INSERT INTO page_templates (page_id, template_content)
VALUES ('406454305878420', 'Xin chào {{ten_khach}}, chào mừng đến với {{ten_shop}}!');

INSERT INTO sender (sender_id, name, avatar) 
VALUES ('406454305878420', 'Test Page', 'https://example.com/avatar.png');

-- Master data for system_variables
INSERT INTO system_variables (variable_key, display_name) VALUES ('ten_khach', 'Tên khách hàng');
INSERT INTO system_variables (variable_key, display_name) VALUES ('ten_shop', 'Tên shop');

-- Tạo bảng facebook_comments với trường is_hidden
CREATE TABLE IF NOT EXISTS facebook_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id VARCHAR(255) UNIQUE,
    post_id VARCHAR(255),
    message TEXT,
    created_time TIMESTAMP
);

-- Cập nhật bảng comment để thêm trường isHidden (nếu có)
ALTER TABLE facebook_comments ADD COLUMN is_hidden BOOLEAN DEFAULT FALSE;

                            