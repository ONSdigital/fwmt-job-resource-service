SET SCHEMA 'gateway';

ALTER TABLE gateway.jobs
    ADD COLUMN TIMESTAMP last_update;