DELETE FROM TRANSACTIONS;

DELETE FROM WALLETS;

INSERT INTO
    WALLETS (
        ID, FULL_NAME, CPF, EMAIL, "PASSWORD", "TYPE", BALANCE, "VERSION"
    )
VALUES (
        1, 'Dejair - User', 12345678900, 'dejair@email.com', '123456', 1, 1000.00, 1
    );

INSERT INTO
    WALLETS (
        ID, FULL_NAME, CPF, EMAIL, "PASSWORD", "TYPE", BALANCE, "VERSION"
    )
VALUES (
        2, 'João do caminhão - Merchant', 12345678901, 'joao@email.com', '123456', 2, 1000.00, 1
    );