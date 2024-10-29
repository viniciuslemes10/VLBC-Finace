CREATE TABLE tb_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_creation DATETIME,
    update_date DATETIME,
    type VARCHAR(50) NOT NULL,
    method VARCHAR(50) NOT NULL,
    value DECIMAL(19, 2) NOT NULL,
    user_id BIGINT,
    categoria_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES tb_users(id),
    FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id)
);