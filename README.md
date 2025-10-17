# Parking System - Java JDBC

A simple parking management system using Java, JDBC, and JOptionPane for UI.

## Features

- **Operator Authentication**: Login with email and password (BCrypt encryption)
  - Email format validation (regex)
  - Password minimum length validation
  - Maximum 3 login attempts
- **Vehicle Entry**: Register vehicle entry with automatic type detection
  - License plate format validation
  - Duplicate open ticket detection
  - Automatic monthly subscription detection
- **Vehicle Exit**: Process vehicle exit with payment calculation
  - Duration calculation
  - Rate calculation with grace period
  - **Monthly subscriptions: $0.00 charge** (as specified)
  - Automatic payment recording
- **License Plate Validation**:
  - Car: 3 letters + 3 numbers (e.g., ABC123)
  - Motorcycle: 3 letters + 2 numbers + 1 letter (e.g., ABC12D)
- **QR Code Data**: String format (TICKET:id|PLATE:plate|DATE:timestamp)
- **Error Handling**: Custom exceptions for validation, authentication, business rules, and data access
- **Dependency Injection**: Clean architecture with DI pattern

## Technologies

- Java 21
- PostgreSQL 16
- JDBC
- BCrypt (password hashing)
- JOptionPane (UI)

## Database Setup

1. Install PostgreSQL
2. Create database:
```sql
CREATE DATABASE crudpark;
```

3. Run the DDL script:
```bash
psql -U postgres -d crudpark -f src/main/resources/DDL.sql
```

4. Insert a test operator (password: "admin123"):
```sql
-- BCrypt hash with cost factor 11 (2^11 = 2,048 rounds)
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
```

5. Insert default rate:
```sql
INSERT INTO rates (hourly_rate, fraction_rate, daily_cap, effective_from, is_active, created_at, updated_at)
VALUES (5.00, 3.00, 50.00, NOW(), true, NOW(), NOW());
```

## Configuration

Edit `src/main/resources/database.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/crudpark
db.username=postgres
db.password=postgres
```

## Build and Run

### Using Maven:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="app.Main"
```

### Or build JAR:
```bash
mvn clean package
java -jar target/crudpark-java-1.0-SNAPSHOT.jar
```

## Usage

1. **Login**:
   - Email: admin@parking.com
   - Password: admin123

2. **Vehicle Entry**:
   - Select "Vehicle Entry"
   - Enter license plate (e.g., ABC123 or ABC12D)
   - System validates format and detects vehicle type
   - Ticket is generated with QR code data

3. **Vehicle Exit**:
   - Select "Vehicle Exit"
   - Enter Ticket ID
   - System calculates duration and payment
   - Shows detailed breakdown

## Project Structure

```
src/
├── main/
│   ├── java/app/
│   │   ├── Main.java                    # Application entry point with DI
│   │   ├── controller/
│   │   │   ├── AuthController.java      # Authentication controller
│   │   │   └── ParkingController.java   # Parking operations controller
│   │   ├── dao/
│   │   │   ├── OperatorDAO.java         # Operator data access
│   │   │   ├── VehicleDAO.java          # Vehicle data access
│   │   │   ├── TicketDAO.java           # Ticket data access
│   │   │   ├── SubscriptionDAO.java     # Subscription data access
│   │   │   ├── RateDAO.java             # Rate data access
│   │   │   └── PaymentDAO.java          # Payment data access
│   │   ├── database/
│   │   │   └── DatabaseConnection.java  # Database connection utility
│   │   ├── exception/
│   │   │   ├── ValidationException.java
│   │   │   ├── AuthenticationException.java
│   │   │   ├── BusinessException.java
│   │   │   ├── DataAccessException.java
│   │   │   └── NotFoundException.java
│   │   ├── model/
│   │   │   ├── Operator.java
│   │   │   ├── Vehicle.java
│   │   │   ├── Ticket.java
│   │   │   └── Rate.java
│   │   ├── service/
│   │   │   ├── AuthService.java         # Business logic for auth
│   │   │   └── ParkingService.java      # Business logic for parking
│   │   └── view/
│   │       ├── LoginView.java           # Login UI
│   │       ├── MainMenuView.java        # Main menu UI
│   │       ├── VehicleEntryView.java    # Entry UI
│   │       └── VehicleExitView.java     # Exit UI
│   └── resources/
│       ├── database.properties
│       └── DDL.sql
```

## Architecture

### Layered Architecture:
```
┌─────────────────────────────────────────────────────┐
│                    VIEW LAYER                       │
│  (JOptionPane dialogs - User interaction)          │
│  LoginView | MainMenuView | EntryView | ExitView   │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│               CONTROLLER LAYER                      │
│  (Input validation & Error mapping)                 │
│  - Validate: null, empty, negative, format          │
│  - Map exceptions to user messages                  │
│  AuthController | ParkingController                 │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                SERVICE LAYER                        │
│  (Business logic & Transactions)                    │
│  - Business rules (grace period, subscriptions)     │
│  - Transaction management (commit/rollback)         │
│  - Rate calculation                                 │
│  AuthService | ParkingService                       │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                  DAO LAYER                          │
│  (Database access - SQL queries)                    │
│  - CRUD operations                                  │
│  - No business logic                                │
│  OperatorDAO | VehicleDAO | TicketDAO | ...         │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│               DATABASE LAYER                        │
│  PostgreSQL Database (crudpark)                     │
└─────────────────────────────────────────────────────┘
```

### Dependency Injection:
```
Main.java (DI Container)
 │
 ├─> DAOs (instantiated first)
 ├─> Services (inject DAOs)
 ├─> Controllers (inject Services)
 └─> Views (no dependencies)
```

### Validation Rules:
- **Controllers**: Validate null, empty, negative values, formats (regex)
- **Services**: Business rules and transaction management
- **DAOs**: Data persistence only

### Exception Handling:
- `ValidationException`: Input validation errors
- `AuthenticationException`: Login failures  
- `BusinessException`: Business rule violations
- `DataAccessException`: Database errors
- `NotFoundException`: Resource not found

## License Plate Format

- **Car**: `ABC123` (3 uppercase letters + 3 digits)
- **Motorcycle**: `ABC12D` (3 uppercase letters + 2 digits + 1 uppercase letter)
- Invalid formats are rejected with error message

## Pricing

- **Grace Period**: First 30 minutes FREE
- **Hourly Rate**: $5.00 per hour
- **Fraction Rate**: $3.00 per additional fraction
- **Daily Cap**: $50.00 maximum
- **Monthly Subscription**: FREE parking

## Notes

- QR code is stored as string format (no image generation)
- All UI interactions use JOptionPane dialogs
- Simple JDBC connections (no pooling)
- BCrypt for secure password storage
- Automatic vehicle type detection