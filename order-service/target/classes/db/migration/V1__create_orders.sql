CREATE TABLE IF NOT EXISTS orders (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_email    VARCHAR(255) NOT NULL,
    status        VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_amount  NUMERIC(19, 2) NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS order_items (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    sku         VARCHAR(100) NOT NULL,
    quantity    INTEGER NOT NULL,
    unit_price  NUMERIC(19, 2) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_orders_user_email ON orders(user_email);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
