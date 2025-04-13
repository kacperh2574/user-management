CREATE TABLE IF NOT EXISTS subscriptions
(
    id                       UUID PRIMARY KEY,
    user_id                  UUID                NOT NULL,
    plan                     VARCHAR(255)        NOT NULL,
    status                   VARCHAR(255)        NOT NULL,
    start_date               DATE                NOT NULL,
    end_date                 DATE
);

INSERT INTO subscriptions (id, user_id, plan, status, start_date, end_date)
SELECT '123e4567-e89b-12d3-a456-426614174000',
       '123e4567-e89b-12d3-a456-426614174001',
       'FREE',
       'ACTIVE',
       '2025-04-12',
       '2030-04-12'
WHERE NOT EXISTS (SELECT 1
                  FROM subscriptions
                  WHERE id = '123e4567-e89b-12d3-a456-426614174000');