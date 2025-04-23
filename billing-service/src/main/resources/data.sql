CREATE TABLE IF NOT EXISTS subscriptions
(
    id               UUID PRIMARY KEY,
    user_id          UUID           NOT NULL,
    plan_type        VARCHAR(255)   NOT NULL,
    pro_status       VARCHAR(255),
    pro_start_date   DATE,
    pro_end_date     DATE,
    created_at       DATE           NOT NULL
);

INSERT INTO subscriptions (
    id, user_id, plan_type, pro_status, pro_start_date, pro_end_date, created_at
)
SELECT
    '123e4567-e89b-12d3-a456-426614174000',
    '123e4567-e89b-12d3-a456-426614174001',
    'FREE',
    NULL,
    NULL,
    NULL,
    '2025-04-22'
WHERE NOT EXISTS (
    SELECT 1 FROM subscriptions
    WHERE id = '123e4567-e89b-12d3-a456-426614174000'
);
