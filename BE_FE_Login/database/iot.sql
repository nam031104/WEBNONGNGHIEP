CREATE DATABASE IF NOT EXISTS iot
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE iot;

CREATE TABLE IF NOT EXISTS account (
    id_account VARCHAR(36) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    number_phone VARCHAR(20),
    location VARCHAR(255)
);
