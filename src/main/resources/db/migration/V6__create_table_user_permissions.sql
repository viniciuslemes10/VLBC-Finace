CREATE TABLE user_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    permissions_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES tb_users(id),
    FOREIGN KEY (permissions_id) REFERENCES tb_permissions(id)
);