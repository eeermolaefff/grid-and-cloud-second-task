
CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    name varchar(30) NOT NULL,
    surname varchar(30) NOT NULL,
    balance float DEFAULT 0,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS accounts_idx ON accounts(name, surname);

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW(); 
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp_accounts
BEFORE UPDATE ON accounts
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

INSERT INTO accounts(name, surname, balance) VALUES
    ('Elizabeth', 'Taylor', 20000.00),
    ('Emma', 'Brown', 1101.5),
    ('Helen', 'Anderson', 1500.00),
    ('James', 'Johnson', 12300.35),
    ('Stephen', 'Brown', 10.00),
    ('Will', 'Smith', 50000.00);

