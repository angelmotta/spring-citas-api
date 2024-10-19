CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    dni VARCHAR(50) NOT NULL,
    specialty_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY(id)
);