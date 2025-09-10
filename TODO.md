# TODO: Increase Test Coverage to 90%

## Current Coverage: ~25-30%
Target: 90%

## Steps to Add Tests

### 1. R2DBC Adapters (infrastructure/driven-adapters/r2dbc-postgresql/src/test/java/co/com/bancolombia/r2dbc)
- [ ] PostgresqlConnectionPropertiesTest.java
- [ ] PostgreSQLConnectionPoolTest.java
- [ ] JwtTokenAdapterTest.java
- [ ] UserAdapterTest.java
- [ ] AuthenticationAdapterTest.java
- [ ] SolicitudAdapterTest.java
- [ ] TransactionalAdapterTest.java

### 2. Mappers (infrastructure/driven-adapters/r2dbc-postgresql/src/test/java/co/com/bancolombia/r2dbc/mapper)
- [ ] UserRequestMapperImplTest.java
- [ ] RoleMapperImplTest.java
- [ ] UserMapperImplTest.java

### 3. API Configs (infrastructure/entry-points/reactive-web/src/test/java/co/com/bancolombia/api/config)
- [ ] CorsConfigTest.java
- [ ] WebClientConfigTest.java
- [ ] LoanAppPathTest.java
- [ ] SecurityHeadersConfigTest.java
- [ ] SecurityConfigTest.java

### 4. API Filters (infrastructure/entry-points/reactive-web/src/test/java/co/com/bancolombia/api/filter)
- [ ] JwtAuthenticationFilterTest.java
- [ ] GlobalExceptionFilterTest.java

### 5. API Utils (infrastructure/entry-points/reactive-web/src/test/java/co/com/bancolombia/api/util)
- [ ] RequestValidatorTest.java
- [ ] AuthenticationUtilTest.java

### 6. API Handlers (infrastructure/entry-points/reactive-web/src/test/java/co/com/bancolombia/api)
- [ ] RouterRestTest.java

### 7. Exceptions (domain/exception/src/test/java/co/com/bancolombia/exception)
- [ ] BaseExceptionTest.java
- [ ] InvalidCredentialsExceptionTest.java
- [ ] InsufficientPrivilegesExceptionTest.java
- [ ] TokenNotFoundExceptionTest.java
- [ ] InvalidTokenExceptionTest.java
- [ ] EmailAlreadyExistsExceptionTest.java
- [ ] AuthenticationExceptionTest.java
- [ ] MissingFieldExceptionTest.java
- [ ] UserNotAuthenticatedExceptionTest.java

### 8. Use Cases (domain/usecase/src/test/java/co/com/bancolombia/usecase)
- [ ] UserUseCaseTest.java (if not fully covered)

### 9. Configs (applications/app-service/src/test/java/co/com/bancolombia/config)
- [ ] ObjectMapperConfigTest.java
- [ ] UseCasesConfigTest.java

### 10. Main Application (applications/app-service/src/test/java/co/com/bancolombia)
- [ ] MainApplicationTest.java

### 11. Models (domain/model/src/test/java/co/com/bancolombia/model)
- [ ] JwtPayloadTest.java (if not fully covered)
- [ ] UserTest.java (add more if needed)
- [ ] RoleTest.java (add more if needed)

### 12. DTOs (domain/dto/src/test/java/co/com/bancolombia/dto)
- [ ] UserRegisterRequestTest.java
- [ ] SessionResponseTest.java

## Follow-up Steps
- [ ] Run tests after each batch of additions
- [ ] Generate Jacoco report to check progress
- [ ] Adjust tests if coverage not increasing
- [ ] Repeat until 90% coverage is reached
