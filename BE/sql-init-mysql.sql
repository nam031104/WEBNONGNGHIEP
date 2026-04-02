CREATE DATABASE IF NOT EXISTS iot;
USE iot;

CREATE TABLE IF NOT EXISTS account (
    id_account VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    number_phone VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    location VARCHAR(255),
    role VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO account (
    id_account,
    username,
    password,
    name,
    number_phone,
    email,
    location,
    role
)
VALUES (
        'acc-admin',
        'admin',
        '$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy5QFM2',
        'Administrator',
        NULL,
        'admin@example.com',
        NULL,
        'ADMIN'
    ),
    (
        'acc-john',
        'john_doe',
        '$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy5QFM2',
        'John Doe',
        NULL,
        'john@example.com',
        NULL,
        'USER'
    )
ON DUPLICATE KEY UPDATE id_account = id_account;
