CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    added_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    added_by VARCHAR(100) NOT NULL
);

-- Create Product Price Table
CREATE TABLE product_price (
    product_id INT PRIMARY KEY,
    price DECIMAL(10, 2) NOT NULL,
    discount_percent DECIMAL(5, 2) DEFAULT 0,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);


-- Create Product Price Change Log Table
CREATE TABLE product_price_change_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    old_price DECIMAL(10, 2),
    new_price DECIMAL(10, 2),
    old_discount DECIMAL(5, 2),
    new_discount DECIMAL(5, 2),
    operation_type ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    operation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    performed_by VARCHAR(100) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- Trigger for Insert Operation on product_price Table
DELIMITER //
CREATE TRIGGER product_price_insert_trigger
AFTER INSERT ON product_price
FOR EACH ROW
BEGIN
    INSERT INTO product_price_change_log (
        product_id,
        old_price,
        new_price,
        old_discount,
        new_discount,
        operation_type,
        performed_by
    )
    VALUES (
        NEW.product_id,
        NULL,
        NEW.price,
        NULL,
        NEW.discount_percent,
        'INSERT',
        NEW.updated_by
    );
END;
//
DELIMITER ;

-- Trigger for Update Operation on product_price Table
DELIMITER //
CREATE TRIGGER product_price_update_trigger
AFTER UPDATE ON product_price
FOR EACH ROW
BEGIN
    INSERT INTO product_price_change_log (
        product_id,
        old_price,
        new_price,
        old_discount,
        new_discount,
        operation_type,
        performed_by
    )
    VALUES (
        NEW.product_id,
        OLD.price,
        NEW.price,
        OLD.discount_percent,
        NEW.discount_percent,
        'UPDATE',
        NEW.updated_by
    );
END;
//
DELIMITER ;

-- Trigger for Delete Operation on product_price Table
DELIMITER //
CREATE TRIGGER product_price_delete_trigger
AFTER DELETE ON product_price
FOR EACH ROW
BEGIN
    INSERT INTO product_price_change_log (
        product_id,
        old_price,
        new_price,
        old_discount,
        new_discount,
        operation_type,
        performed_by
    )
    VALUES (
        OLD.product_id,
        OLD.price,
        NULL,
        OLD.discount_percent,
        NULL,
        'DELETE',
        OLD.updated_by
    );
END;
//
DELIMITER ;

-- Inserting records into the "product" table
INSERT INTO product (product_id, name, category, added_by)
VALUES
    (1, 'Laptop', 'Electronics', 'Admin'),
    (2, 'Smartphone', 'Electronics', 'User123'),
    (3, 'Headphones', 'Electronics', 'Admin'),
    (4, 'Tablet', 'Electronics', 'User123'),
    (5, 'T-shirt', 'Apparel', 'Admin'),
    (6, 'Jeans', 'Apparel', 'User456'),
    (7, 'Running Shoes', 'Footwear', 'Admin'),
    (8, 'Sneakers', 'Footwear', 'User789'),
    (9, 'Backpack', 'Accessories', 'Admin'),
    (10, 'Watch', 'Accessories', 'User123');

-- Inserting records into the "product_price" table
INSERT INTO product_price (product_id, price, discount_percent, updated_by)
VALUES
    (1, 999.99, 10.00, 'Admin'),
    (2, 699.00, 5.00, 'User123'),
    (3, 149.99, 20.00, 'Admin'),
    (4, 299.99, 15.00, 'User123'),
    (5, 20.00, 0.00, 'Admin'),
    (6, 49.99, 10.00, 'User456'),
    (7, 79.99, 5.00, 'Admin'),
    (8, 89.00, 8.00, 'User789'),
    (9, 40.00, 0.00, 'Admin'),
    (10, 250.00, 10.00, 'User123');
   
-- Update the price and discount for a product
UPDATE product_price
SET price = 799.99, discount_percent = 12.00, updated_by = 'Admin'
WHERE product_id = 1;

-- Update the discount for another product
UPDATE product_price
SET discount_percent = 8.00, updated_by = 'User123'
WHERE product_id = 2;

-- Delete the price record for a product
DELETE FROM product_price
WHERE product_id = 3;

-- Delete the price record for another product
DELETE FROM product_price
WHERE product_id = 4;


select * from product;
select * from product_price;
select * from product_price_change_log;

-- Query to Join product and product_price Tables
SELECT p.name, p.category, pp.price, pp.updated_by, pp.updated_on
FROM product p
JOIN product_price pp ON p.product_id = pp.product_id;
