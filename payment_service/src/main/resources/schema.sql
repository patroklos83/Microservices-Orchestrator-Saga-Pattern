DROP TABLE IF EXISTS customerAccounts;

CREATE TABLE customerAccounts (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    customer_id INT,
    balance decimal
);
