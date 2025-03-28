ALTER TABLE buses
ADD COLUMN departure_date VARCHAR(10) NOT NULL DEFAULT '25-03-2024';

-- Update existing records to have today's date
UPDATE buses
SET departure_date = '25-03-2024'
WHERE departure_date = '25-03-2024'; 