CREATE OR REPLACE FUNCTION find_wallet_by_id(id bigint)
RETURNS boolean
LANGUAGE plpgsql
AS
$$
BEGIN
RETURN (
    SELECT COUNT(*) = 1
    FROM wallets w
    WHERE w.id = find_wallet_by_id.id
);
END;
$$;


SELECT find_wallet_by_id(1);


select * from wallets w

CREATE OR REPLACE PROCEDURE transfer(
    p_source_id BIGINT,
    p_dest_id BIGINT,
    p_amount DECIMAL,
    p_source_version BIGINT,
    p_dest_version BIGINT
)
LANGUAGE plpgsql
AS $$
DECLARE
v_source_balance DECIMAL;
    v_dest_balance DECIMAL;
BEGIN
    -- 1) Kiểm tra ví source
    IF NOT find_wallet_by_id(p_source_id) THEN
        RAISE EXCEPTION 'Source wallet % not found', p_source_id;
END IF;

    -- 2) Kiểm tra ví destination
    IF NOT find_wallet_by_id(p_dest_id) THEN
        RAISE EXCEPTION 'Destination wallet % not found', p_dest_id;
END IF;

    -- 3) Lấy số dư + lock row
SELECT available_balance INTO v_source_balance
FROM wallets WHERE id = p_source_id FOR UPDATE;

SELECT available_balance INTO v_dest_balance
FROM wallets WHERE id = p_dest_id FOR UPDATE;

-- 4) Kiểm tra số dư
IF v_source_balance < p_amount THEN
        RAISE EXCEPTION 'Insufficient balance in source wallet';
END IF;

    -- 5) Trừ tiền (optimistic locking)
UPDATE wallets
SET available_balance = available_balance - abs(p_amount),
    version = version + 1,
    updated_at = (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint
WHERE id = p_source_id AND version = p_source_version;

IF NOT FOUND THEN
        RAISE EXCEPTION 'Source wallet version mismatch';
END IF;

    -- 6) Cộng tiền (optimistic locking)
UPDATE wallets
SET available_balance = available_balance + abs(p_amount),
    version = version + 1,
    updated_at = (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint
WHERE id = p_dest_id AND version = p_dest_version;

IF NOT FOUND THEN
        RAISE EXCEPTION 'Destination wallet version mismatch';
END IF;

    RAISE NOTICE 'Transfer OK: % -> % : amount %', p_source_id, p_dest_id, p_amount;

END;
$$;

CALL transfer(1, 2, 1000, 4, 5);
