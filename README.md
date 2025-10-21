# 🚗 CrudPark - Parking Management System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

Complete parking management system developed in Java with Swing GUI, PostgreSQL database, and modern MVC architecture.

## ✨ Features

### 🔐 Operator Authentication
- Secure login with email and password
- BCrypt password encryption
- Email format validation (regex)
- Maximum 3 login attempts
- Session tracking with operator ID

### 🚙 Vehicle Entry Registration
- Automatic vehicle type detection
- License plate format validation:
  - **Cars**: `ABC123` (3 letters + 3 numbers)
  - **Motorcycles**: `ABC12D` (3 letters + 2 numbers + 1 letter)
- Automatic monthly subscription detection
- Duplicate open ticket prevention
- QR code generation
- Ticket printing
- Modern Swing GUI interface

### 🚗 Vehicle Exit Registration - **NEW FEATURE**
- **Dual search**: By ticket ID OR license plate
- **Payment preview** before confirming
- **Payment method selector**:
  - Cash (Efectivo)
  - Card (Tarjeta)
- **Auto-selection**: If amount is $0, automatically selects "Cash"
- Automatic duration and rate calculation
- Grace period handling (first 15 minutes free)
- Monthly subscriptions: **$0.00** (free)
- Detailed confirmation dialogs
- Receipt printing option

### 💰 Rate System
- Configurable grace period (15 minutes default)
- Differentiated hourly rates:
  - Cars: $5.00/hour
  - Motorcycles: $3.00/hour
- Daily maximum cap
- Automatic fraction calculation

### 🌐 Spanish-English Translation
- Database in English (international standard)
- User interface in Spanish
- Automatic translation of:
  - Ticket types (Guest/Invitado, Monthly/Mensualidad)
  - Payment methods (Cash/Efectivo, Card/Tarjeta)

## 🛠 Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming language |
| Maven | 3.x | Dependency management & build |
| PostgreSQL | 16 | Relational database |
| JDBC | 42.7.4 | Database connectivity |
| HikariCP | 5.1.0 | Connection pooling |
| BCrypt | 0.10.2 | Password encryption |
| ZXing | 3.5.3 | QR code generation |
| Swing | Built-in | Graphical user interface |
| JUnit 5 | 5.10.0 | Testing framework |
| Mockito | 5.5.0 | Mocking for tests |
| SLF4J | 2.0.9 | Logging system |

## Exception Handling

```java
ValidationException      → Input validation errors
AuthenticationException  → Authentication failures
BusinessException        → Business rule violations
NotFoundException        → Resource not found
DataAccessException      → Database errors
```

## 📦 Prerequisites

- **Java Development Kit (JDK)**: 21 or higher
- **Apache Maven**: 3.6 or higher
- **PostgreSQL**: 16 or higher
- **Git**: For cloning the repository

### Verify installations:

```bash
java --version    # Should show Java 21+
mvn --version     # Should show Maven 3.6+
psql --version    # Should show PostgreSQL 16+
```

## 🚀 Installation

### 1. Clone the repository

```bash
git clone https://github.com/SJB-Parking/crudpark-java.git
cd crudpark-java
```

### 2. Configure the database

#### Create the database:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE crudpark_sjb;
\q
```

#### Run the DDL script:

```bash
psql -U postgres -d crudpark_sjb -f src/main/resources/DDL.sql
```

#### Insert test data (optional):

```bash
psql -U postgres -d crudpark_sjb -f src/main/resources/test-data.sql
```

### 3. Configure credentials

Edit `src/main/resources/database.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/crudpark_sjb
db.username=postgres
db.password=your_password_here
```

### 4. Compile the project

```bash
mvn clean compile
```

## ⚙️ Configuration

### Default Credentials

If you ran `test-data.sql`, you can use:

```
Email: admin@parking.com
Password: admin123
```

### Create a New Operator Manually

```sql
INSERT INTO operators (full_name, email, username, password_hash, is_active)
VALUES (
  'John Doe',
  'john@parking.com',
  'jdoe',
  '$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.',  -- admin123
  true
);
```

### Configure Rates

```sql
-- Rate for cars
INSERT INTO rates (vehicle_type, hourly_rate, grace_period_minutes, daily_cap, is_active)
VALUES ('Car', 5.00, 15, 50.00, true);

-- Rate for motorcycles
INSERT INTO rates (vehicle_type, hourly_rate, grace_period_minutes, daily_cap, is_active)
VALUES ('Motorcycle', 3.00, 15, 30.00, true);
```

## 🎮 Usage

### Run the application

```bash
# Using Maven
mvn exec:java

# Or build JAR and run
mvn clean package
java -jar target/parking-1.0-SNAPSHOT.jar
```

### Workflow

#### 1️⃣ Login
1. Open the application
2. Enter email and password
3. Click "Iniciar Sesión" (Login)

#### 2️⃣ Register Vehicle Entry
1. In the main menu, click "Registrar Entrada" (Register Entry)
2. Enter vehicle license plate (e.g., `ABC123` or `ABC12D`)
3. System automatically detects vehicle type
4. Confirm registration
5. Ticket is displayed with:
   - Ticket ID
   - License plate
   - Vehicle type
   - Ticket type (Guest/Monthly)
   - Entry time
   - QR code

#### 3️⃣ Register Vehicle Exit - **IMPROVED PROCESS**

**Option A: Search by Ticket ID**
1. Click "Registrar Salida" (Register Exit)
2. Select "Buscar por ID de Ticket" (Search by Ticket ID)
3. Enter the ID (e.g., `123`)
4. System shows **EXIT SUMMARY**:
   - Ticket information
   - Parking duration
   - **Amount to pay**
   - **Payment method selector** (if amount > 0)
5. Select payment method:
   - Efectivo (Cash)
   - Tarjeta (Card)
6. Click "Confirmar Salida" (Confirm Exit)
7. System processes and shows **EXIT PROCESSED**
8. Option to print receipt

**Option B: Search by License Plate**
1. Click "Registrar Salida" (Register Exit)
2. Select "Buscar por Placa" (Search by Plate)
3. Enter plate (e.g., `ABC123`)
4. System searches for open ticket with that plate
5. Continues same as Option A (from step 4)

**Special Cases:**
- **Monthly subscription**: Amount $0, payment method auto-selected as "Cash"
- **Grace period** (< 15 min): Amount $0, free
- **Normal parking**: Automatic calculation based on rate

## 🧪 Testing

### Run all tests

```bash
mvn test
```

### Run specific tests

```bash
# Controller tests
mvn test -Dtest=ParkingControllerTest

# Swing controller tests
mvn test -Dtest=SwingVehicleExitControllerTest

# Translator tests
mvn test -Dtest=VehicleTypeTranslatorTest
```

### Test Coverage

- ✅ **ParkingControllerTest**: 17 tests
  - Input validation
  - Exit preview by ID
  - Exit preview by plate
  - Processing with payment method
  - Error handling

- ✅ **SwingVehicleExitControllerTest**: 11 tests
  - Exit processing
  - Preview by ID and plate
  - Payment methods
  - Exception handling

**Total**: 28 passing tests ✅

## 📁 Project Structure

```
crudpark-java/
├── src/
│   ├── main/
│   │   ├── java/app/
│   │   │   ├── Main.java                           # Entry point with DI
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java             # Authentication
│   │   │   │   ├── ParkingController.java          # Parking operations
│   │   │   │   ├── SwingVehicleExitController.java # Swing controller
│   │   │   │   ├── EntryResult.java                # Result DTO
│   │   │   │   ├── ExitResult.java                 # Result DTO
│   │   │   │   └── LoginResult.java                # Result DTO
│   │   │   ├── dao/
│   │   │   │   ├── OperatorDAO.java
│   │   │   │   ├── VehicleDAO.java
│   │   │   │   ├── TicketDAO.java                  # + findOpenTicketByPlate()
│   │   │   │   ├── RateDAO.java
│   │   │   │   ├── PaymentDAO.java
│   │   │   │   └── SubscriptionDAO.java
│   │   │   ├── database/
│   │   │   │   └── DatabaseConnection.java         # HikariCP pool
│   │   │   ├── exception/
│   │   │   │   ├── ValidationException.java
│   │   │   │   ├── AuthenticationException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── DataAccessException.java
│   │   │   │   └── NotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── Operator.java
│   │   │   │   ├── Vehicle.java
│   │   │   │   ├── Ticket.java
│   │   │   │   ├── Rate.java
│   │   │   │   └── Payment.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   └── ParkingService.java             # + preview methods
│   │   │   ├── util/
│   │   │   │   ├── Logger.java
│   │   │   │   ├── QRCodeGenerator.java
│   │   │   │   ├── TicketPrinter.java
│   │   │   │   └── VehicleTypeTranslator.java      # ES ↔ EN
│   │   │   └── view/swing/
│   │   │       ├── LoginFrame.java                 # Login UI
│   │   │       ├── MainMenuFrame.java              # Main menu
│   │   │       ├── VehicleEntryFrame.java          # Entry registration
│   │   │       ├── VehicleExitFrame.java           # Exit registration (NEW)
│   │   │       ├── PaymentPreviewDialog.java       # Payment preview (NEW)
│   │   │       ├── VehicleExitSuccessDialog.java   # Confirmation (NEW)
│   │   │       ├── LoginView.java
│   │   │       ├── MainMenuView.java
│   │   │       ├── VehicleEntryView.java
│   │   │       └── VehicleExitView.java            # View logic
│   │   └── resources/
│   │       ├── database.properties                  # DB configuration
│   │       ├── DDL.sql                             # Database schema
│   │       └── test-data.sql                       # Test data
│   └── test/
│       └── java/app/
│           ├── controller/
│           │   ├── ParkingControllerTest.java       # 17 tests ✅
│           │   └── SwingVehicleExitControllerTest.java # 11 tests ✅
├── target/                                          # Compiled files
├── logs/                                            # Application logs
├── qr-codes/                                        # Generated QR codes
├── pom.xml                                          # Maven config
├── README.md                                        # This file
└── QUICKSTART.md                                    # Quick start guide
```

## 🎯 Advanced Features

### HikariCP Connection Pool

```java
// Optimized configuration
Minimum Idle: 2
Maximum Pool Size: 10
Connection Timeout: 30 seconds
Idle Timeout: 10 minutes
Max Lifetime: 30 minutes
```

### Logging System

```java
// Log levels
INFO  - Normal operations
WARN  - Warnings (e.g., tickets not found)
ERROR - Critical errors
DEBUG - Development information
```

Logs saved in: `logs/app.log`

### Dependency Injection

```java
// Main.java initializes all dependencies
DAOs → Services → Controllers → Views
```

### Multi-Layer Validation

1. **Controller**: Format, null, empty, range
2. **Service**: Business rules
3. **DAO**: Database constraints

### Automatic Translation

```java
// Database storage (English)
"Cash" → Efectivo
"Card" → Tarjeta
"Guest" → Invitado
"Monthly" → Mensualidad

// UI in Spanish
Efectivo → "Cash" (for DB)
Tarjeta → "Card" (for DB)
```

## 📊 Database Model

### Main Tables

```sql
operators         -- System operators
vehicles          -- Registered vehicles
tickets           -- Entry/exit tickets
rates             -- Rates per vehicle type
payments          -- Recorded payments
subscriptions     -- Monthly subscriptions
```

### Relationships

```
operators (1) ──< (N) tickets
vehicles  (1) ──< (N) tickets
tickets   (1) ──< (1) payments
vehicles  (1) ──< (N) subscriptions
rates     (1) ──< (N) tickets
```

## 🤝 Contributing

### How to Contribute

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Style Guide

- Follow Java conventions (camelCase, PascalCase)
- Spanish comments for complex logic
- Spanish JavaDoc for public methods
- Tests for new features
- Clean and readable code

## 👥 Team

Developed by **SJB-Parking**

- GitHub: [@SJB-Parking](https://github.com/SJB-Parking)


---

