DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS items;


CREATE TABLE orders (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    customer_id INT,
    item_id UUID,
    price NUMERIC(10, 2),
    status VARCHAR(50),
    created timestamp
);

CREATE TABLE items (
    item_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    price NUMERIC(10, 2)
);