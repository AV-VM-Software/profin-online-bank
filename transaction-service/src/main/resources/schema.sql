CREATE TABLE IF NOT EXISTS transactions (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            user_id INT,
                                            recipient_id INT,
                                            id_sender_account VARCHAR(255),
    id_recipient_account VARCHAR(255),
    transaction_type VARCHAR(50),
    payment_status VARCHAR(20),
    AMOUNT DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
