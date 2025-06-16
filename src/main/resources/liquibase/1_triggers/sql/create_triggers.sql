CREATE TABLE triggers (
      id UUID PRIMARY KEY,
      chat_id BIGINT NOT NULL,
      position_name VARCHAR(255) NOT NULL,
      asset_name VARCHAR(255) NOT NULL,
      upper_bound NUMERIC(20, 8) NOT NULL,
      lower_bound NUMERIC(20, 8) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
