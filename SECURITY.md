# BCrypt Password Hashing - Security Documentation

## Overview

This project uses **BCrypt** for secure password hashing with a **cost factor of 11**.

## What is BCrypt?

BCrypt is a password hashing function designed by Niels Provos and David Mazières. It's based on the Blowfish cipher and incorporates a salt to protect against rainbow table attacks.

## Cost Factor Explained

### Cost Factor: 11

```
Cost Factor 11 = 2^11 = 2,048 rounds
```

The cost factor determines how many iterations the hashing algorithm performs. Higher values = more secure but slower.

### Why Cost Factor 11?

| Cost Factor | Rounds | Time (approx) | Security Level |
|-------------|--------|---------------|----------------|
| 10          | 1,024  | ~100ms        | Good           |
| **11**      | **2,048** | **~200ms** | **Better** ✅ |
| 12          | 4,096  | ~400ms        | Best           |
| 13          | 8,192  | ~800ms        | Overkill       |

We chose **11** because it provides:
- ✅ Excellent security against brute force attacks
- ✅ Reasonable performance (~200ms per hash)
- ✅ Good balance for production use

## Hash Structure

```
$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.
 │   │   │                       │
 │   │   │                       └─ Hash (31 chars)
 │   │   └─ Salt (22 chars)
 │   └─ Cost Factor (11 = 2^11 rounds)
 └─ BCrypt Version (2a)
```

### Components:

1. **$2a$** - BCrypt algorithm version
2. **11** - Cost factor (2^11 = 2,048 iterations)
3. **kH3ulu5AEQGioyzXDx.pg.** - Random salt (22 characters)
4. **4JDqE9/mACqZbtdymRdAm.zgUN2rX7.** - Final hash (31 characters)

## Implementation

### Hashing (Creating new passwords):

```java
// In AuthService.java
public static String hashPassword(String password) {
    return BCrypt.withDefaults().hashToString(11, password.toCharArray());
}
```

**Example:**
```java
String plainPassword = "admin123";
String hash = AuthService.hashPassword(plainPassword);
// Returns: $2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.
```

### Verification (Login):

```java
// In AuthService.login()
BCrypt.Result result = BCrypt.verifyer().verify(
    password.toCharArray(), 
    operator.getPasswordHash()
);

if (!result.verified) {
    throw new AuthenticationException("Invalid email or password");
}
```

## Security Benefits

### 1. **Salting**
Each password gets a unique random salt, preventing:
- Rainbow table attacks
- Pre-computed hash attacks
- Identical password detection

### 2. **Adaptive Hashing**
Cost factor can be increased over time as hardware improves:
```java
// Future-proof: increase cost factor as needed
BCrypt.withDefaults().hashToString(12, password.toCharArray()); // Even more secure
```

### 3. **Slow by Design**
2,048 rounds means:
- Legitimate user: 200ms delay (acceptable)
- Attacker: 200ms per attempt (discouraging brute force)

### Attack Resistance:

| Attack Type | Without BCrypt | With BCrypt (factor 11) |
|-------------|----------------|-------------------------|
| Brute Force | Millions/sec   | ~5 attempts/sec        |
| Rainbow Tables | Instant      | Impossible (salted)    |
| Dictionary  | Seconds       | Years                  |

## Performance Considerations

### Single Login:
```
Cost Factor 11 ≈ 200ms per verification
```
**Impact:** Negligible for user experience

### Bulk Operations:
```
1,000 hashes = 200 seconds (3.3 minutes)
```
**Recommendation:** Use async/batch processing for bulk password operations

## Usage in Project

### 1. Generate Hash (Admin Tool)

Create a simple utility class:

```java
package app.util;

import app.service.AuthService;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java PasswordHashGenerator <password>");
            return;
        }
        
        String password = args[0];
        String hash = AuthService.hashPassword(password);
        
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
    }
}
```

**Run:**
```bash
mvn compile
java -cp target/classes app.util.PasswordHashGenerator "newpassword123"
```

### 2. Insert New Operator

```sql
INSERT INTO operators (full_name, email, username, password_hash, is_active, created_at, updated_at)
VALUES (
    'New Operator',
    'operator@parking.com',
    'operator',
    '$2a$11$GENERATED_HASH_HERE',  -- Use PasswordHashGenerator
    true,
    NOW(),
    NOW()
);
```

## Best Practices

### ✅ DO:
- Use cost factor 11 or higher
- Let BCrypt generate random salts
- Store full hash string (includes version, cost, salt, hash)
- Use `BCrypt.verifyer().verify()` for comparison
- Never log or expose password hashes

### ❌ DON'T:
- Use MD5 or SHA1 for passwords
- Implement custom crypto
- Store plain text passwords
- Use cost factor < 10
- Reuse salts across passwords

## Testing

### Verify Hash Generation:

```java
@Test
public void testPasswordHashing() {
    String password = "admin123";
    String hash = AuthService.hashPassword(password);
    
    // Verify format
    assertTrue(hash.startsWith("$2a$11$"));
    assertEquals(60, hash.length());
    
    // Verify verification
    BCrypt.Result result = BCrypt.verifyer().verify(
        password.toCharArray(), 
        hash
    );
    assertTrue(result.verified);
}
```

### Test Login with Correct/Wrong Password:

```java
@Test
public void testLoginWithCorrectPassword() {
    // password: admin123
    // hash: $2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.
    
    Operator operator = authService.login("admin@parking.com", "admin123");
    assertNotNull(operator);
}

@Test(expected = AuthenticationException.class)
public void testLoginWithWrongPassword() {
    authService.login("admin@parking.com", "wrongpassword");
}
```

## Migration Guide

If you need to upgrade cost factor in the future:

### 1. Update Code:
```java
// Change from 11 to 12
BCrypt.withDefaults().hashToString(12, password.toCharArray());
```

### 2. Rehash on Next Login:
```java
public Operator login(String email, String password) {
    // ... existing login logic ...
    
    // Check if rehashing needed
    if (needsRehash(operator.getPasswordHash())) {
        String newHash = hashPassword(password);
        operatorDAO.updatePasswordHash(operator.getId(), newHash);
    }
    
    return operator;
}

private boolean needsRehash(String hash) {
    // Check if cost factor is less than current standard
    return !hash.startsWith("$2a$12$");
}
```

## References

- [BCrypt Wikipedia](https://en.wikipedia.org/wiki/Bcrypt)
- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
- [BCrypt Java Library](https://github.com/patrickfav/bcrypt)

## Summary

| Property | Value |
|----------|-------|
| Algorithm | BCrypt |
| Version | 2a |
| Cost Factor | **11** (2,048 rounds) |
| Hash Time | ~200ms |
| Hash Length | 60 characters |
| Security | High (OWASP recommended) |

---

**Remember:** Security is not just about the algorithm, but proper implementation and handling throughout the application lifecycle.
