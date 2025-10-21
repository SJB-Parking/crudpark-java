# Quick Start Guide - Parking System

## Prerequisites

- ✅ Java 21 or higher
- ✅ PostgreSQL 16 or higher
- ✅ Maven 3.6 or higher

## Setup Steps

### 1. Database Setup

#### Create Database:
```bash
psql -U postgres
```

```sql
CREATE DATABASE crudpark;
\q
```

#### Run DDL Script:
```bash
psql -U postgres -d crudpark -f src/main/resources/DDL.sql
```

#### Insert Test Data:
```bash
psql -U postgres -d crudpark -f src/main/resources/test-data.sql
```

### 2. Configure Database Connection

Edit `src/main/resources/database.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/crudpark
db.username=postgres
db.password=YOUR_PASSWORD_HERE
```

### 3. Build Project

```bash
mvn clean install
```

### 4. Run Application

```bash
mvn exec:java -Dexec.mainClass="app.Main"
```

Or build JAR and run:
```bash
mvn clean package
java -jar target/crudpark-java-1.0-SNAPSHOT.jar
```

## Test Credentials

### Login:
- **Email**: `admin@parking.com`
- **Password**: `admin123`

### Test Vehicles:
1. **ABC123** (Car) - Has active monthly subscription (**$0.00 charge**)
2. **XYZ456** (Car) - No subscription (normal charges)
3. **DEF12G** (Motorcycle) - No subscription (normal charges)

## Usage Flow

### 1. Login
- Enter email: `admin@parking.com`
- Enter password: `admin123`
- Click OK

### 2. Vehicle Entry
1. Select "Vehicle Entry"
2. Enter license plate (e.g., `ABC123`, `XYZ456`, or `DEF12G`)
3. System will:
   - Validate format
   - Detect vehicle type
   - Check for monthly subscription
   - Create ticket
   - Show ticket details with QR code data

### 3. Vehicle Exit
1. Select "Vehicle Exit"
2. Enter Ticket ID (from entry screen)
3. System will:
   - Calculate duration
   - Calculate charges:
     - **Monthly subscription: $0.00** ✅
     - Grace period (≤30 min): $0.00
     - Regular: Hourly rate + fraction
   - Show payment details

### 4. Logout
- Select "Logout"
- Application will close

## Testing Scenarios

### Scenario 1: Monthly Subscription (FREE)
```
1. Entry: ABC123
2. Wait a few minutes
3. Exit: Use ticket ID from step 1
4. Expected: $0.00 charge (Monthly Subscription)
```

### Scenario 2: Grace Period (FREE)
```
1. Entry: XYZ456
2. Wait < 30 minutes
3. Exit: Use ticket ID from step 1
4. Expected: $0.00 charge (Grace Period)
```

### Scenario 3: Normal Parking (CHARGED)
```
1. Entry: XYZ456
2. Wait > 30 minutes
3. Exit: Use ticket ID from step 1
4. Expected: Calculated charge based on:
   - Hourly rate: $5.00/hour
   - Fraction rate: $3.00/fraction
   - Daily cap: $50.00
```

### Scenario 4: Invalid License Plate
```
1. Entry: 123ABC (invalid format)
2. Expected: Validation error message
```

### Scenario 5: Duplicate Entry
```
1. Entry: ABC123
2. Entry: ABC123 (without exit)
3. Expected: "Vehicle already has an open ticket"
```

## Validation Testing

### Email Validation:
- ❌ Empty: "Email cannot be empty"
- ❌ Invalid format: "Invalid email format"
- ✅ Valid: admin@parking.com

### Password Validation:
- ❌ Empty: "Password cannot be empty"
- ❌ Too short: "Password must be at least 6 characters"
- ✅ Valid: admin123

### License Plate Validation:
- ❌ Empty: "License plate cannot be empty"
- ❌ Wrong length: "License plate must be 6 characters"
- ❌ Invalid format: "Invalid license plate format..."
- ✅ Car: ABC123
- ✅ Motorcycle: ABC12D

### Ticket ID Validation:
- ❌ Empty: "Ticket ID cannot be empty"
- ❌ Non-numeric: "Ticket ID must be a valid number"
- ❌ Negative/Zero: "Ticket ID must be greater than 0"
- ✅ Valid: 1, 2, 3...

## Troubleshooting

### "Error accessing database"
- Check PostgreSQL is running
- Verify database.properties credentials
- Ensure database exists: `psql -U postgres -l | grep crudpark`

### "No active rate found"
- Run test-data.sql to insert default rate
- Or manually insert: `INSERT INTO rates...`

### "Operator account is inactive"
- Update operator: `UPDATE operators SET is_active = true WHERE email = 'admin@parking.com';`

### "Ticket not found"
- Verify ticket ID exists: `SELECT * FROM tickets WHERE id = ?;`

### Maven build fails
- Ensure Java 21 is installed: `java -version`
- Update Maven: `mvn -version`
- Clear Maven cache: `mvn clean`

## Database Queries for Testing

### Check all tickets:
```sql
SELECT 
    t.id,
    t.folio,
    v.license_plate,
    t.ticket_type,
    t.status,
    t.entry_datetime,
    t.exit_datetime
FROM tickets t
JOIN vehicles v ON t.vehicle_id = v.id
ORDER BY t.id DESC;
```

### Check payments:
```sql
SELECT 
    p.id,
    t.folio,
    v.license_plate,
    p.amount,
    p.payment_method,
    p.payment_datetime
FROM payments p
JOIN tickets t ON p.ticket_id = t.id
JOIN vehicles v ON t.vehicle_id = v.id
ORDER BY p.id DESC;
```

### Check active subscriptions:
```sql
SELECT 
    ms.id,
    c.full_name,
    v.license_plate,
    ms.start_date,
    ms.end_date,
    ms.is_active
FROM monthly_subscriptions ms
JOIN customers c ON ms.customer_id = c.id
JOIN subscription_vehicles sv ON ms.id = sv.subscription_id
JOIN vehicles v ON sv.vehicle_id = v.id
WHERE ms.is_active = true;
```

## Project Structure Overview

```
app/
├── Main.java                    # Entry point with DI
├── controller/                  # Input validation
│   ├── AuthController
│   └── ParkingController
├── dao/                         # Database access
│   ├── OperatorDAO
│   ├── VehicleDAO
│   ├── TicketDAO
│   ├── SubscriptionDAO
│   ├── RateDAO
│   └── PaymentDAO
├── service/                     # Business logic
│   ├── AuthService
│   └── ParkingService
├── view/                        # JOptionPane UI
│   ├── LoginView
│   ├── MainMenuView
│   ├── VehicleEntryView
│   └── VehicleExitView
├── model/                       # Entities
│   ├── Operator
│   ├── Vehicle
│   ├── Ticket
│   └── Rate
└── exception/                   # Custom exceptions
    ├── ValidationException
    ├── AuthenticationException
    ├── BusinessException
    ├── DataAccessException
    └── NotFoundException
```

## Next Steps

1. ✅ Setup database and insert test data
2. ✅ Configure database.properties
3. ✅ Build and run application
4. ✅ Test all scenarios
5. ⬜ Add more operators
6. ⬜ Add more monthly subscriptions
7. ⬜ Customize rates
8. ⬜ Generate reports

## Support

For issues or questions:
1. Check ARCHITECTURE.md for design details
2. Check IMPLEMENTATION.md for complete feature list
3. Review error messages - they're descriptive
4. Check database with SQL queries above

---

**Happy Parking! 🚗🏍️**
