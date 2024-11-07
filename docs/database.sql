CREATE DATABASE IF NOT EXISTS prod_db;

USE prod_db;

CREATE TABLE IF NOT EXISTS users (
user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
email varchar(255) NOT NULL,
password varchar(255) NOT NULL,
first_name varchar(255) NOT NULL,
last_name varchar(255)
);

CREATE TABLE IF NOT EXISTS wishlists (
wishlist_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
user_id INT NOT NULL,
wishlist_name varchar(255) NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS wishes (
wish_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
wishlist_id INT NOT NULL,
wish_name varchar(255) NOT NULL,
wish_description TEXT,
wish_price INT,
wish_url VARCHAR(255),
reserved BOOLEAN DEFAULT FALSE,
reserved_by_user_id INT,
FOREIGN KEY (wishlist_id) REFERENCES wishlists(wishlist_id),
FOREIGN KEY (reserved_by_user_id) REFERENCES users(user_id)
);
