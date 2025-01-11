CREATE TABLE IF NOT EXISTS transactions (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT,
        recipient_id BIGINT,
        id_sender_account BIGINT,
        id_recipient_account BIGINT,
        transaction_type VARCHAR(50),
        payment_status VARCHAR(50),
        amount DECIMAL(19,2),
        created_at TIMESTAMP
    );
