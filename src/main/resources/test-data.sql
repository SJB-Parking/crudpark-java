-- ============================================
-- CRUDPARK - TEST DATA SETUP
-- Complete test data for parking management system
-- ============================================

-- ============================================
-- 1. OPERATORS - System Users
-- ============================================
-- Password for all: admin123 (BCrypt hash with cost factor 11)
INSERT INTO operators (full_name, email, username, password_hash, is_active, created_at, updated_at)
VALUES 
    (
        'Administrator System',
        'admin@parking.com',
        'admin',
        '$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.',
        true,
        NOW(),
        NOW()
    ),
    (
        'Maria Rodriguez',
        'maria.rodriguez@parking.com',
        'mrodriguez',
        '$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.',
        true,
        NOW(),
        NOW()
    ),
    (
        'Carlos Mendez',
        'carlos.mendez@parking.com',
        'cmendez',
        '$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.',
        true,
        NOW(),
        NOW()
    ),
    (
        'Ana Garcia',
        'ana.garcia@parking.com',
        'agarcia',
        '$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.',
        false,
        NOW(),
        NOW()
    );

-- ============================================
-- 2. RATES - Parking Rates by Vehicle Type
-- ============================================
INSERT INTO rates (rate_name, vehicle_type, hourly_rate, fraction_rate, daily_cap, grace_period_minutes, is_active, effective_from, created_at, updated_at)
VALUES 
    (
        'Standard Car Rate',
        'Car',
        5.00,     -- $5.00 per hour
        2.50,     -- $2.50 per fraction
        50.00,    -- $50.00 daily cap
        15,       -- 15 minutes grace period
        true,
        NOW(),
        NOW(),
        NOW()
    ),
    (
        'Standard Motorcycle Rate',
        'Motorcycle',
        3.00,     -- $3.00 per hour
        1.50,     -- $1.50 per fraction
        30.00,    -- $30.00 daily cap
        15,       -- 15 minutes grace period
        true,
        NOW(),
        NOW(),
        NOW()
    ),
    (
        'Premium Car Rate (Inactive)',
        'Car',
        8.00,     -- $8.00 per hour
        4.00,     -- $4.00 per fraction
        80.00,    -- $80.00 daily cap
        10,       -- 10 minutes grace period
        false,
        NOW() - INTERVAL '30 days',
        NOW(),
        NOW()
    );

-- ============================================
-- 3. CUSTOMERS - Registered Customers
-- ============================================
INSERT INTO customers (full_name, email, phone, identification_number, is_active, created_at, updated_at)
VALUES 
    (
        'John Alexander Doe',
        'john.doe@email.com',
        '+1 (555) 123-4567',
        '123456789',
        true,
        NOW(),
        NOW()
    ),
    (
        'Sarah Michelle Johnson',
        'sarah.johnson@email.com',
        '+1 (555) 234-5678',
        '987654321',
        true,
        NOW(),
        NOW()
    ),
    (
        'Michael Robert Smith',
        'michael.smith@email.com',
        '+1 (555) 345-6789',
        '456789123',
        true,
        NOW(),
        NOW()
    ),
    (
        'Emma Patricia Williams',
        'emma.williams@email.com',
        '+1 (555) 456-7890',
        '789123456',
        true,
        NOW(),
        NOW()
    ),
    (
        'David James Brown',
        'david.brown@email.com',
        '+1 (555) 567-8901',
        '321654987',
        false,
        NOW(),
        NOW()
    );

-- ============================================
-- 4. VEHICLES - Registered Vehicles
-- ============================================
INSERT INTO vehicles (license_plate, vehicle_type, brand, model, color, created_at, updated_at)
VALUES 
    -- Cars with monthly subscriptions
    ('ABC123', 'Car', 'Toyota', 'Camry 2023', 'Silver', NOW(), NOW()),
    ('XYZ789', 'Car', 'Honda', 'Accord 2022', 'Black', NOW(), NOW()),
    ('LMN456', 'Car', 'Ford', 'Focus 2021', 'Blue', NOW(), NOW()),
    
    -- Motorcycles with monthly subscriptions
    ('MTO12A', 'Motorcycle', 'Yamaha', 'YZF-R3', 'Red', NOW(), NOW()),
    ('MTO34B', 'Motorcycle', 'Kawasaki', 'Ninja 400', 'Green', NOW(), NOW()),
    
    -- Cars without subscription (guest)
    ('GUE111', 'Car', 'Chevrolet', 'Malibu 2020', 'White', NOW(), NOW()),
    ('GUE222', 'Car', 'Nissan', 'Altima 2023', 'Gray', NOW(), NOW()),
    ('GUE333', 'Car', 'Mazda', 'CX-5 2022', 'Red', NOW(), NOW()),
    ('GUE444', 'Car', 'Hyundai', 'Elantra 2021', 'Blue', NOW(), NOW()),
    
    -- Motorcycles without subscription (guest)
    ('GMT45C', 'Motorcycle', 'Honda', 'CBR500R', 'Black', NOW(), NOW()),
    ('GMT67D', 'Motorcycle', 'Suzuki', 'GSX-R600', 'Blue', NOW(), NOW()),
    ('GMT89E', 'Motorcycle', 'Ducati', 'Monster 821', 'Red', NOW(), NOW());

-- ============================================
-- 5. MONTHLY SUBSCRIPTIONS - Active Plans
-- ============================================
INSERT INTO monthly_subscriptions (customer_id, subscription_code, start_date, end_date, is_active, amount_paid, max_vehicles, created_at, updated_at)
VALUES 
    (
        1,  -- John Doe
        'MONTH-2025-001',
        NOW() - INTERVAL '5 days',
        NOW() + INTERVAL '25 days',
        true,
        150.00,
        2,  -- Can register 2 vehicles
        NOW() - INTERVAL '5 days',
        NOW()
    ),
    (
        2,  -- Sarah Johnson
        'MONTH-2025-002',
        NOW() - INTERVAL '10 days',
        NOW() + INTERVAL '20 days',
        true,
        120.00,
        1,  -- Can register 1 vehicle
        NOW() - INTERVAL '10 days',
        NOW()
    ),
    (
        3,  -- Michael Smith
        'MONTH-2025-003',
        NOW() - INTERVAL '2 days',
        NOW() + INTERVAL '28 days',
        true,
        180.00,
        3,  -- Can register 3 vehicles
        NOW() - INTERVAL '2 days',
        NOW()
    ),
    (
        5,  -- David Brown (inactive customer)
        'MONTH-2024-999',
        NOW() - INTERVAL '60 days',
        NOW() - INTERVAL '30 days',
        false,
        100.00,
        1,
        NOW() - INTERVAL '60 days',
        NOW()
    );

-- ============================================
-- 6. SUBSCRIPTION VEHICLES - Vehicle Links
-- ============================================
INSERT INTO subscription_vehicles (subscription_id, vehicle_id, added_at)
VALUES 
    -- John Doe's vehicles (2)
    (1, (SELECT id FROM vehicles WHERE license_plate = 'ABC123'), NOW() - INTERVAL '5 days'),
    (1, (SELECT id FROM vehicles WHERE license_plate = 'XYZ789'), NOW() - INTERVAL '4 days'),
    
    -- Sarah Johnson's vehicle (1)
    (2, (SELECT id FROM vehicles WHERE license_plate = 'LMN456'), NOW() - INTERVAL '10 days'),
    
    -- Michael Smith's vehicles (2 of 3 allowed)
    (3, (SELECT id FROM vehicles WHERE license_plate = 'MTO12A'), NOW() - INTERVAL '2 days'),
    (3, (SELECT id FROM vehicles WHERE license_plate = 'MTO34B'), NOW() - INTERVAL '1 day');

-- ============================================
-- 7. CUSTOMER VEHICLES - Primary Vehicle Flag
-- ============================================
INSERT INTO customer_vehicles (customer_id, vehicle_id, is_primary, created_at)
VALUES 
    -- John Doe's vehicles
    (1, (SELECT id FROM vehicles WHERE license_plate = 'ABC123'), true, NOW() - INTERVAL '5 days'),
    (1, (SELECT id FROM vehicles WHERE license_plate = 'XYZ789'), false, NOW() - INTERVAL '4 days'),
    
    -- Sarah Johnson's vehicle
    (2, (SELECT id FROM vehicles WHERE license_plate = 'LMN456'), true, NOW() - INTERVAL '10 days'),
    
    -- Michael Smith's vehicles
    (3, (SELECT id FROM vehicles WHERE license_plate = 'MTO12A'), true, NOW() - INTERVAL '2 days'),
    (3, (SELECT id FROM vehicles WHERE license_plate = 'MTO34B'), false, NOW() - INTERVAL '1 day');

-- ============================================
-- 8. TICKETS - Entry/Exit Records
-- ============================================
-- Closed tickets (completed parking sessions)
INSERT INTO tickets (folio, vehicle_id, operator_id, subscription_id, entry_datetime, exit_datetime, ticket_type, status, parking_duration_minutes, qr_code_data, created_at, updated_at)
VALUES 
    -- Guest tickets (closed)
    (
        'TKT-2025-0001',
        (SELECT id FROM vehicles WHERE license_plate = 'GUE111'),
        1,  -- admin operator
        NULL,
        NOW() - INTERVAL '3 hours',
        NOW() - INTERVAL '1 hour',
        'Guest',
        'Closed',
        120,  -- 2 hours
        'QR-TKT-2025-0001-GUE111',
        NOW() - INTERVAL '3 hours',
        NOW() - INTERVAL '1 hour'
    ),
    (
        'TKT-2025-0002',
        (SELECT id FROM vehicles WHERE license_plate = 'GUE222'),
        2,  -- Maria Rodriguez
        NULL,
        NOW() - INTERVAL '5 hours',
        NOW() - INTERVAL '2 hours',
        'Guest',
        'Closed',
        180,  -- 3 hours
        'QR-TKT-2025-0002-GUE222',
        NOW() - INTERVAL '5 hours',
        NOW() - INTERVAL '2 hours'
    ),
    (
        'TKT-2025-0003',
        (SELECT id FROM vehicles WHERE license_plate = 'GMT45C'),
        1,
        NULL,
        NOW() - INTERVAL '2 hours',
        NOW() - INTERVAL '30 minutes',
        'Guest',
        'Closed',
        90,  -- 1.5 hours
        'QR-TKT-2025-0003-GMT45C',
        NOW() - INTERVAL '2 hours',
        NOW() - INTERVAL '30 minutes'
    ),
    
    -- Monthly subscription tickets (closed)
    (
        'TKT-2025-0004',
        (SELECT id FROM vehicles WHERE license_plate = 'ABC123'),
        1,
        1,  -- John Doe's subscription
        NOW() - INTERVAL '1 day',
        NOW() - INTERVAL '23 hours',
        'Monthly',
        'Closed',
        60,
        'QR-TKT-2025-0004-ABC123',
        NOW() - INTERVAL '1 day',
        NOW() - INTERVAL '23 hours'
    );

-- Open tickets (currently parked)
INSERT INTO tickets (folio, vehicle_id, operator_id, subscription_id, entry_datetime, exit_datetime, ticket_type, status, parking_duration_minutes, qr_code_data, created_at, updated_at)
VALUES 
    -- Guest vehicles currently parked
    (
        'TKT-2025-0005',
        (SELECT id FROM vehicles WHERE license_plate = 'GUE333'),
        2,
        NULL,
        NOW() - INTERVAL '45 minutes',
        NULL,
        'Guest',
        'Open',
        NULL,
        'QR-TKT-2025-0005-GUE333',
        NOW() - INTERVAL '45 minutes',
        NOW() - INTERVAL '45 minutes'
    ),
    (
        'TKT-2025-0006',
        (SELECT id FROM vehicles WHERE license_plate = 'GUE444'),
        3,
        NULL,
        NOW() - INTERVAL '2 hours 30 minutes',
        NULL,
        'Guest',
        'Open',
        NULL,
        'QR-TKT-2025-0006-GUE444',
        NOW() - INTERVAL '2 hours 30 minutes',
        NOW() - INTERVAL '2 hours 30 minutes'
    ),
    (
        'TKT-2025-0007',
        (SELECT id FROM vehicles WHERE license_plate = 'GMT67D'),
        1,
        NULL,
        NOW() - INTERVAL '10 minutes',
        NULL,
        'Guest',
        'Open',
        NULL,
        'QR-TKT-2025-0007-GMT67D',
        NOW() - INTERVAL '10 minutes',
        NOW() - INTERVAL '10 minutes'
    ),
    
    -- Monthly subscription vehicles currently parked
    (
        'TKT-2025-0008',
        (SELECT id FROM vehicles WHERE license_plate = 'XYZ789'),
        2,
        1,  -- John Doe's subscription
        NOW() - INTERVAL '3 hours',
        NULL,
        'Monthly',
        'Open',
        NULL,
        'QR-TKT-2025-0008-XYZ789',
        NOW() - INTERVAL '3 hours',
        NOW() - INTERVAL '3 hours'
    ),
    (
        'TKT-2025-0009',
        (SELECT id FROM vehicles WHERE license_plate = 'LMN456'),
        1,
        2,  -- Sarah Johnson's subscription
        NOW() - INTERVAL '1 hour 15 minutes',
        NULL,
        'Monthly',
        'Open',
        NULL,
        'QR-TKT-2025-0009-LMN456',
        NOW() - INTERVAL '1 hour 15 minutes',
        NOW() - INTERVAL '1 hour 15 minutes'
    ),
    (
        'TKT-2025-0010',
        (SELECT id FROM vehicles WHERE license_plate = 'MTO12A'),
        3,
        3,  -- Michael Smith's subscription
        NOW() - INTERVAL '5 hours 45 minutes',
        NULL,
        'Monthly',
        'Open',
        NULL,
        'QR-TKT-2025-0010-MTO12A',
        NOW() - INTERVAL '5 hours 45 minutes',
        NOW() - INTERVAL '5 hours 45 minutes'
    );

-- ============================================
-- 9. PAYMENTS - Payment Records
-- ============================================
INSERT INTO payments (ticket_id, operator_id, amount, payment_method, payment_datetime, created_at)
VALUES 
    (
        (SELECT id FROM tickets WHERE folio = 'TKT-2025-0001'),
        1,
        10.00,  -- 2 hours * $5/hour for car
        'Cash',
        NOW() - INTERVAL '1 hour',
        NOW() - INTERVAL '1 hour'
    ),
    (
        (SELECT id FROM tickets WHERE folio = 'TKT-2025-0002'),
        2,
        15.00,  -- 3 hours * $5/hour for car
        'Card',
        NOW() - INTERVAL '2 hours',
        NOW() - INTERVAL '2 hours'
    ),
    (
        (SELECT id FROM tickets WHERE folio = 'TKT-2025-0003'),
        1,
        4.50,   -- 1.5 hours * $3/hour for motorcycle
        'Cash',
        NOW() - INTERVAL '30 minutes',
        NOW() - INTERVAL '30 minutes'
    ),
    (
        (SELECT id FROM tickets WHERE folio = 'TKT-2025-0004'),
        1,
        0.00,   -- Monthly subscription = $0
        'Cash',
        NOW() - INTERVAL '23 hours',
        NOW() - INTERVAL '23 hours'
    );

-- ============================================
-- 10. SHIFTS - Operator Work Shifts
-- ============================================
INSERT INTO shifts (operator_id, shift_start, shift_end, initial_cash, final_cash, status, created_at)
VALUES 
    -- Completed shifts
    (
        1,  -- admin
        NOW() - INTERVAL '2 days',
        NOW() - INTERVAL '2 days' + INTERVAL '8 hours',
        100.00,
        245.50,
        'Closed',
        NOW() - INTERVAL '2 days'
    ),
    (
        2,  -- Maria Rodriguez
        NOW() - INTERVAL '1 day',
        NOW() - INTERVAL '1 day' + INTERVAL '8 hours',
        150.00,
        320.00,
        'Closed',
        NOW() - INTERVAL '1 day'
    ),
    
    -- Active shifts
    (
        1,  -- admin
        NOW() - INTERVAL '3 hours',
        NULL,
        200.00,
        NULL,
        'Open',
        NOW() - INTERVAL '3 hours'
    ),
    (
        3,  -- Carlos Mendez
        NOW() - INTERVAL '2 hours',
        NULL,
        150.00,
        NULL,
        'Open',
        NOW() - INTERVAL '2 hours'
    );

-- ============================================
-- VERIFICATION QUERIES
-- ============================================
-- Uncomment these to verify data insertion

-- -- Active operators
-- SELECT id, full_name, email, username, is_active FROM operators;

-- -- Active rates
-- SELECT id, rate_name, vehicle_type, hourly_rate, grace_period_minutes, is_active FROM rates WHERE is_active = true;

-- -- All vehicles
-- SELECT id, license_plate, vehicle_type, brand, model FROM vehicles ORDER BY vehicle_type, license_plate;

-- -- Active customers with subscriptions
-- SELECT 
--     c.id,
--     c.full_name,
--     c.email,
--     ms.subscription_code,
--     ms.start_date,
--     ms.end_date,
--     ms.amount_paid
-- FROM customers c
-- JOIN monthly_subscriptions ms ON c.id = ms.customer_id
-- WHERE ms.is_active = true;

-- -- Open tickets (currently parked)
-- SELECT 
--     t.id,
--     t.folio,
--     v.license_plate,
--     v.vehicle_type,
--     t.ticket_type,
--     t.entry_datetime,
--     EXTRACT(EPOCH FROM (NOW() - t.entry_datetime))/60 as minutes_parked
-- FROM tickets t
-- JOIN vehicles v ON t.vehicle_id = v.id
-- WHERE t.status = 'Open'
-- ORDER BY t.entry_datetime;

-- -- Payment summary
-- SELECT 
--     payment_method,
--     COUNT(*) as total_payments,
--     SUM(amount) as total_amount
-- FROM payments
-- GROUP BY payment_method;

-- ============================================
-- SUMMARY & NOTES
-- ============================================
/*
LOGIN CREDENTIALS:
  Email: admin@parking.com
  Username: admin
  Password: admin123
  
  Other operators:
  - maria.rodriguez@parking.com / mrodriguez / admin123
  - carlos.mendez@parking.com / cmendez / admin123

VEHICLE TYPES & RATES:
  Cars:
    - Hourly Rate: $5.00
    - Fraction Rate: $2.50
    - Daily Cap: $50.00
    - Grace Period: 15 minutes
  
  Motorcycles:
    - Hourly Rate: $3.00
    - Fraction Rate: $1.50
    - Daily Cap: $30.00
    - Grace Period: 15 minutes

MONTHLY SUBSCRIPTIONS (Active):
  - John Doe: ABC123, XYZ789 (2 vehicles)
  - Sarah Johnson: LMN456 (1 vehicle)
  - Michael Smith: MTO12A, MTO34B (2 vehicles)

CURRENTLY PARKED (Open Tickets):
  Guest:
    - GUE333 (45 minutes) - Within grace period
    - GUE444 (2.5 hours) - Normal rate
    - GMT67D (10 minutes) - Within grace period
  
  Monthly:
    - XYZ789 (3 hours) - $0.00 (subscription)
    - LMN456 (1h 15min) - $0.00 (subscription)
    - MTO12A (5h 45min) - $0.00 (subscription)

TEST SCENARIOS:
  1. Register entry: Use GUE111, GUE222, GMT89E (not currently parked)
  2. Process exit by ID: Use open ticket IDs (5, 6, 7, 8, 9, 10)
  3. Process exit by Plate: Use GUE333, GUE444, GMT67D, XYZ789, LMN456, MTO12A
  4. Grace period test: GMT67D (10 min) should be $0.00
  5. Monthly subscription test: XYZ789, LMN456, MTO12A should be $0.00
  6. Normal rate test: GUE444 (2.5 hours) should be $12.50

DATABASE STATISTICS:
  - Operators: 4 (3 active, 1 inactive)
  - Rates: 3 (2 active, 1 inactive)
  - Customers: 5 (4 active, 1 inactive)
  - Vehicles: 12 (5 cars, 7 motorcycles)
  - Monthly Subscriptions: 4 (3 active, 1 expired)
  - Tickets: 10 (4 closed, 6 open)
  - Payments: 4
  - Shifts: 4 (2 closed, 2 open)
*/
