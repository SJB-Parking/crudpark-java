-- Migration script to change qr_code_data from VARCHAR to BYTEA
-- Execute this in your PostgreSQL database

-- Change the column type
ALTER TABLE tickets ALTER COLUMN qr_code_data TYPE BYTEA USING qr_code_data::bytea;

-- Verify the change
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name = 'tickets' AND column_name = 'qr_code_data';
