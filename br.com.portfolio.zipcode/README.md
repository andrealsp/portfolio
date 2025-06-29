# Brazilian Address Lookup Service

A REST API service built with Spring Boot that provides Brazilian address information through ZIP code (CEP) lookups and address-based searches.

## Overview

This service offers a unified interface to retrieve standardized Brazilian address information, featuring:
- ZIP code-based lookups
- Address component-based searches
- Standardized response format
- Robust error handling
- External postal service integration

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Internet connection

### Running the Application
```bash
# Clone the repository
git clone https://github.com/portfolio/br.com.portfolio.zipcode.git

# Navigate to project directory
cd br.com.portfolio.zipcode

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The service will start on `http://localhost:8080`

## API Reference

### Address Lookup Endpoint

```http
GET /postalcode/v1/search/zipcode
```

#### Body Parameters

| Parameter | Type   | Description                                        | Required                    |
|-----------|--------|----------------------------------------------------|----------------------------|
| zipcode   | String | Brazilian ZIP code (e.g., "01001000")              | No                         |
| street    | String | Street name                                        | No                         |
| city      | String | City name                                          | No                         |
| state     | String | State abbreviation (e.g., "SP")                    | Yes (if zipcode not provided) |

#### Example Requests

Search by ZIP code:
```http
GET /postalcode/v1/search/zipcode
```

```json
{
   "zipcode": "60130240"
}
```

Search by address components:
```http
GET /postalcode/v1/search/zipcode
```

```json
{
   "streetName": "Amapaenses",
   "city": "Itapevi",
   "state": "SP"
}
```

#### Success Response

```json
[
    {
        "zipcode": "01001000",
        "street": "Praça da Sé",
        "complement": "lado ímpar",
        "neighborhood": "Sé",
        "city": "São Paulo",
        "state": "SP",
        "stateFullName": "São Paulo",
        "region": "Sudeste",
        "areaCode": "11"
    }
]
```

#### Error Response

```json
{
    "timestamp": "2024-06-28T10:30:00.000Z",
    "status": 400,
    "message": "Invalid ZipcodeRequest: when zipcode is not provided, state is required",
    "path": "/api/v1/zipcode"
}
```

### Status Codes

| Status Code | Description                                             |
|-------------|---------------------------------------------------------|
| 200         | Success - Address information retrieved                  |
| 400         | Bad Request - Invalid parameters or missing requirements |
| 404         | Not Found - Address not found                           |
| 500         | Internal Server Error                                   |

## Technical Details

### Technology Stack
- Spring Boot 3.x
- Spring Cloud OpenFeign
- Spring Web
- Lombok
- JUnit 5 & Mockito
- OpenAPI/Swagger

### Project Structure

```
src/
├── main/
│   ├── java/br/com/portfolio/zipcode/
│   │   ├── api/rest/                # REST API controllers
│   │   ├── core/
│   │   │   ├── application/        # Business logic & services
│   │   │   └── domain/            # Domain models
│   │   ├── infrastructure/         # External integrations
│   │   └── shared/                # Utilities & exceptions
│   └── resources/
└── test/                          # Test suites
```

### Key Components

#### API Layer (`api/rest/`)
- `ZipCodeController`: Handles HTTP requests
- `ZipCodeAPI`: API interface definition

#### Core (`core/`)
- `ZipcodeService`: Business logic implementation
- Domain models for request/response handling

#### Infrastructure (`infrastructure/`)
- External service integrations
- REST clients configuration

#### Shared (`shared/`)
- Exception handling
- Common utilities
- Constants

## Error Handling

The service implements a global exception handler that provides standardized error responses for:

1. Validation Errors
   - Missing required parameters
   - Invalid ZIP code format
   - Invalid state codes

2. Business Rule Violations
   - State requirement when ZIP code is not provided
   - Invalid search criteria combinations

3. System Errors
   - External service failures
   - Internal processing errors

## Testing

The project includes comprehensive test coverage:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ZipCodeControllerTest
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Version History

- 1.0.0
  - Initial release
  - Basic ZIP code lookup functionality
  - Address search implementation
  - Standard error handling

## Design Patterns

The project implements several design patterns to ensure clean architecture, maintainability, and separation of concerns:

### Structural Patterns
- **Facade Pattern**: The service layer acts as a facade, providing a simplified interface for the complex subsystem of postal code lookups
- **Adapter Pattern**: Used in the infrastructure layer to adapt external postal service responses to internal domain models

### Behavioral Patterns
- **Strategy Pattern**: Implemented in the search functionality, allowing different search strategies (by ZIP code or by street name)
- **Template Method**: Used in the exception handling structure to standardize error responses

### Creational Patterns
- **Builder Pattern**: Utilized in domain models for object construction (ZipcodeRequest and ZipcodeResponse)
- **Singleton Pattern**: Applied through Spring's dependency injection for service components

### Architectural Patterns
- **Port and Adapters (Hexagonal Architecture)**: The application is structured with clear boundaries between:
  - Core domain logic (core package)
  - External interfaces (api package)
  - Infrastructure concerns (infrastructure package)
- **DTO Pattern**: Used to separate domain models from external API representations
- **Repository Pattern**: Implemented in the infrastructure layer for external service interaction

### Other Patterns
- **Dependency Injection**: Used throughout the application via Spring Framework
- **Global Exception Handler**: Centralized exception handling for consistent error responses
- **Mapper Pattern**: Used to transform between different object representations
