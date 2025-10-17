-- ============================================
-- PARKING SYSTEM - TEST DATA SETUP
-- ============================================

-- 1. Create test operator (password: admin123)
-- Hash generated with BCrypt cost factor 11 (2^11 = 2,048 rounds)
INSERT INTO operators (full_name, email, username, password_hash, is_active, created_at, updated_at)
VALUES (
    'Admin User',
    'admin@parking.com',
    'admin',
    '$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.',
    true,
    NOW(),
    NOW()
);

-- 2. Create default parking rate
INSERT INTO rates (hourly_rate, fraction_rate, daily_cap, effective_from, is_active, created_at, updated_at)
VALUES (
    5.00,    -- $5.00 per hour
    3.00,    -- $3.00 per fraction
    50.00,   -- $50.00 daily cap
    NOW(),   -- Effective immediately
    true,    -- Active
    NOW(),
    NOW()
);

-- 3. Create test vehicles
INSERT INTO vehicles (license_plate, vehicle_type, created_at, updated_at)
VALUES 
    ('ABC123', 'Car', NOW(), NOW()),
    ('XYZ456', 'Car', NOW(), NOW()),
    ('DEF12G', 'Motorcycle', NOW(), NOW());

-- 4. Create a test customer for monthly subscription
INSERT INTO customers (full_name, email, phone_number, created_at, updated_at)
VALUES (
    'John Doe',
    'john.doe@example.com',
    '+1234567890',
    NOW(),
    NOW()
);

-- 5. Create monthly subscription for ABC123
INSERT INTO monthly_subscriptions (
    customer_id,
    start_date,
    end_date,
    amount_paid,
    payment_date,
    is_active,
    created_at,
    updated_at
)
VALUES (
    1,  -- customer_id from previous insert
    NOW(),
    NOW() + INTERVAL '30 days',
    100.00,
    NOW(),
    true,
    NOW(),
    NOW()
);

-- 6. Link vehicle to subscription
INSERT INTO subscription_vehicles (subscription_id, vehicle_id)
VALUES (
    1,  -- subscription_id from previous insert
    (SELECT id FROM vehicles WHERE license_plate = 'ABC123')
);

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Check operators
SELECT id, full_name, email, is_active FROM operators;

-- Check rates
SELECT id, hourly_rate, fraction_rate, daily_cap, is_active FROM rates;

-- Check vehicles
SELECT id, license_plate, vehicle_type FROM vehicles;

-- Check customers
SELECT id, full_name, email FROM customers;

-- Check active subscriptions
SELECT 
    ms.id,
    c.full_name as customer,
    ms.start_date,
    ms.end_date,
    ms.is_active,
    STRING_AGG(v.license_plate, ', ') as vehicles
FROM monthly_subscriptions ms
JOIN customers c ON ms.customer_id = c.id
LEFT JOIN subscription_vehicles sv ON ms.id = sv.subscription_id
LEFT JOIN vehicles v ON sv.vehicle_id = v.id
GROUP BY ms.id, c.full_name, ms.start_date, ms.end_date, ms.is_active;

-- ============================================
-- NOTES
-- ============================================
-- Login Credentials:
--   Email: admin@parking.com
--   Password: admin123
--
-- Test Vehicles:
--   ABC123 - Car (has monthly subscription)
--   XYZ456 - Car (no subscription)
--   DEF12G - Motorcycle (no subscription)
--
-- Expected Behavior:
--   - ABC123: $0.00 charge (monthly subscription)
--   - XYZ456: Normal charges apply
--   - DEF12G: Normal charges apply
-- ============================================
