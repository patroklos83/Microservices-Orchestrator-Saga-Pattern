DROP TABLE IF EXISTS items;

CREATE TABLE items (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    item_id INT,
    price NUMERIC(10, 2),
    stock_available INT
);