SET SCHEMA 'gateway';

CREATE TABLE gateway.input_file (
    id                  bigserial PRIMARY KEY,
    file                bytea NOT NULL,
    filename            varchar UNIQUE NOT NULL,
    file_timestamp      TIMESTAMP,
    received_timestamp  TIMESTAMP
);