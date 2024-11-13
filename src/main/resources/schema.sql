CREATE TABLE IF NOT EXISTS specialties (
    id INTEGER GENERATED ALWAYS AS IDENTITY,
    specialty_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    dni VARCHAR(50) NOT NULL,
    specialty_id INTEGER NOT NULL,
    appointment_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY(id),
    CONSTRAINT fk_specialty
        FOREIGN KEY (specialty_id)
            REFERENCES specialties(id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
