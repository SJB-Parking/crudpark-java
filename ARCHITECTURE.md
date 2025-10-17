# Architecture Documentation - Parking System

## Overview

This parking system follows a **layered architecture** with clear separation of concerns and **dependency injection** pattern.

## Architecture Layers

### 1. View Layer (`app.view`)
- **Purpose**: User interface using JOptionPane dialogs
- **Responsibilities**:
  - Display information to users
  - Collect user input
  - Show success/error messages
- **Components**:
  - `LoginView`: Login dialogs
  - `MainMenuView`: Main menu navigation
  - `VehicleEntryView`: Entry registration UI
  - `VehicleExitView`: Exit processing UI

### 2. Controller Layer (`app.controller`)
- **Purpose**: Bridge between View and Service layers
- **Responsibilities**:
  - **Input validation**: null, empty, negative values, format validation
  - **Error mapping**: Convert exceptions to user-friendly messages
  - **Data formatting**: Prepare data for services
- **Validation Rules**:
  - Email: Required, valid format (regex)
  - Password: Required, minimum 6 characters
  - License Plate: Required, exactly 6 characters
  - Ticket ID: Required, positive integer
  - Operator ID: Positive integer
- **Components**:
  - `AuthController`: Authentication validation
  - `ParkingController`: Parking operations validation

### 3. Service Layer (`app.service`)
- **Purpose**: Business logic implementation
- **Responsibilities**:
  - Apply business rules
  - **Manage transactions** (commit/rollback)
  - Coordinate between multiple DAOs
  - Calculate rates and payments
- **Business Rules**:
  - Grace period: First 30 minutes free
  - **Monthly subscription: $0.00 charge** (FREE)
  - Hourly rate + fraction calculation
  - Daily cap application
  - Duplicate ticket prevention
- **Components**:
  - `AuthService`: Login authentication with BCrypt
  - `ParkingService`: Entry/exit processing, rate calculation

### 4. DAO Layer (`app.dao`)
- **Purpose**: Database access
- **Responsibilities**:
  - CRUD operations
  - SQL query execution
  - Data mapping (ResultSet → Entity)
- **Components**:
  - `OperatorDAO`: Operator queries
  - `VehicleDAO`: Vehicle queries
  - `TicketDAO`: Ticket queries
  - `SubscriptionDAO`: Subscription validation
  - `RateDAO`: Rate retrieval
  - `PaymentDAO`: Payment recording

### 5. Model Layer (`app.model`)
- **Purpose**: Entity representations
- **Components**:
  - `Operator`: User credentials
  - `Vehicle`: License plate, type
  - `Ticket`: Parking ticket
  - `Rate`: Pricing configuration

### 6. Exception Layer (`app.exception`)
- **Purpose**: Custom exception handling
- **Components**:
  - `ValidationException`: Input validation errors
  - `AuthenticationException`: Login failures
  - `BusinessException`: Business rule violations
  - `DataAccessException`: Database errors
  - `NotFoundException`: Resource not found

## Dependency Flow

```
Main (DI Container)
  │
  ├─> Views (UI Display)
  │
  ├─> Controllers
  │     │
  │     └─> Services
  │           │
  │           └─> DAOs
  │                 │
  │                 └─> Database
```

## Dependency Injection

All dependencies are created and injected in `Main.java`:

```java
// DAOs (no dependencies)
operatorDAO = new OperatorDAO();
vehicleDAO = new VehicleDAO();
...

// Services (inject DAOs)
authService = new AuthService(operatorDAO);
parkingService = new ParkingService(vehicleDAO, ticketDAO, ...);

// Controllers (inject Services)
authController = new AuthController(authService);
parkingController = new ParkingController(parkingService);

// Views (no dependencies)
loginView = new LoginView();
...
```

## Request Flow

### Example: Vehicle Entry

```
1. User enters license plate
   └─> VehicleEntryView.showLicensePlateInput()

2. View returns input to Main
   └─> Main.handleVehicleEntry()

3. Main calls Controller with validation
   └─> ParkingController.processEntry(licensePlate, operatorId)
       │
       ├─ Validate: null, empty, length
       ├─ Validate: operatorId > 0
       │
4. Controller calls Service
   └─> ParkingService.processEntry(licensePlate, operatorId)
       │
       ├─ Start transaction
       ├─ Detect vehicle type
       ├─ Find/create vehicle (VehicleDAO)
       ├─ Check open tickets (TicketDAO)
       ├─ Check subscription (SubscriptionDAO)
       ├─ Generate folio (TicketDAO)
       ├─ Create ticket (TicketDAO)
       ├─ Commit transaction
       │
5. Service returns Ticket to Controller
   └─> Controller returns Ticket to Main

6. Main displays result
   └─> VehicleEntryView.showEntrySuccess(ticket)
```

## Error Handling

### Exception Propagation:

```
DAO Layer:
  SQLException → DataAccessException

Service Layer:
  Business rules → BusinessException
  Not found → NotFoundException
  DataAccessException → propagate

Controller Layer:
  Input validation → ValidationException
  All exceptions → propagate

Main:
  Catch all exceptions
  Map to user messages
  Display via View
```

### Error Messages by Layer:

- **ValidationException**: "Email cannot be empty", "Invalid license plate format"
- **AuthenticationException**: "Invalid email or password", "Account inactive"
- **BusinessException**: "Vehicle already has an open ticket"
- **NotFoundException**: "Ticket not found"
- **DataAccessException**: "Error accessing database"

## Transaction Management

Transactions are managed in the **Service layer**:

```java
try (Connection conn = DatabaseConnection.getConnection()) {
    conn.setAutoCommit(false);
    
    try {
        // Multiple DAO operations
        // Business logic
        
        conn.commit();
        
    } catch (Exception e) {
        conn.rollback();
        throw e;
    }
}
```

## Monthly Subscription Logic

When processing exit:

```java
// Check ticket type
if ("Monthly".equals(ticket.getTicketType())) {
    isFree = true;
    freeReason = "Monthly Subscription";
    amount = 0.0;
    // NO payment recorded
}
```

Monthly subscriptions are detected during **entry**:

```java
// Check for active subscription
Integer subscriptionId = subscriptionDAO.findActiveSubscriptionByVehicleId(vehicleId);
String ticketType = subscriptionId != null ? "Monthly" : "Guest";
```

## Best Practices Applied

1. ✅ **Separation of Concerns**: Each layer has single responsibility
2. ✅ **Dependency Injection**: Loose coupling between components
3. ✅ **Exception Handling**: Custom exceptions for different error types
4. ✅ **Input Validation**: Controllers validate all inputs
5. ✅ **Transaction Management**: Services handle database transactions
6. ✅ **Business Logic Isolation**: Services contain all business rules
7. ✅ **Data Access Abstraction**: DAOs isolate SQL queries
8. ✅ **Clean Code**: Meaningful names, single responsibility methods
9. ✅ **Error Mapping**: User-friendly error messages
10. ✅ **MVC Pattern**: Model-View-Controller architecture

## Testing Strategy

### Unit Tests:
- DAOs: Mock database connections
- Services: Mock DAOs
- Controllers: Mock Services
- Views: Mock dialogs

### Integration Tests:
- Controller → Service → DAO → Database
- End-to-end flows

### Test Cases:
- Valid inputs
- Invalid inputs (null, empty, negative)
- Business rule violations
- Database errors
- Edge cases (grace period, daily cap)
