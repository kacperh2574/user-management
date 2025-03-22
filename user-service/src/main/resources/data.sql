CREATE TABLE IF NOT EXISTS users
(
    id                      UUID PRIMARY KEY,
    name                    VARCHAR(255)        NOT NULL,
    email                   VARCHAR(255) UNIQUE NOT NULL,
    address                 VARCHAR(255)        NOT NULL,
    date_of_birth           DATE                NOT NULL,
    date_of_registration    DATE                NOT NULL
);

INSERT INTO users (id, name, email, address, date_of_birth, date_of_registration)
SELECT '123e4567-e89b-12d3-a456-426614174000',
       'John Smith',
       'john.smith@example.com',
       '10 Main St, Summerville',
       '1985-06-15',
       '2025-01-10'
WHERE NOT EXISTS (SELECT 1
                  FROM users
                  WHERE id = '123e4567-e89b-12d3-a456-426614174000');

INSERT INTO users (id, name, email, address, date_of_birth, date_of_registration)
SELECT '123e4567-e89b-12d3-a456-426614174001',
       'Jane Rookie',
       'jane.rookie@example.com',
       '15 Old St, Wintertown',
       '1990-09-23',
       '2024-12-01'
WHERE NOT EXISTS (SELECT 1
                  FROM users
                  WHERE id = '123e4567-e89b-12d3-a456-426614174001');

INSERT INTO users (id, name, email, address, date_of_birth, date_of_registration)
SELECT '123e4567-e89b-12d3-a456-426614174002',
       'Alice Johnson',
       'alice.johnson@example.com',
       '20 Pine St, Springfield',
       '1978-03-12',
       '2024-06-20'
WHERE NOT EXISTS (SELECT 1
                  FROM users
                  WHERE id = '123e4567-e89b-12d3-a456-426614174002');