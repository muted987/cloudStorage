CREATE SCHEMA IF NOT EXISTS user_management;

CREATE TABLE IF NOT EXISTS user_management.t_user(
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL CHECK(length(trim(username)) > 3) UNIQUE,
    password VARCHAR NOT NULL CHECK(length(trim(username)) > 8),
    role VARCHAR NOT NULL
);