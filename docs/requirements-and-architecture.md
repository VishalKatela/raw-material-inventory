# Requirements, Architecture and Technology Setup

## SRS summary
### Functional requirements
- Users can add materials with code, name, category, unit, quantity, and minimum quantity.
- Users can update existing material details.
- Users can create stock-in and stock-out transactions.
- The system calculates stock status based on quantity and minimum threshold.
- Users can search and filter materials.
- The system shows alerts for low stock and out-of-stock exceptions.

### Non-functional requirements
- data should be persistent and auditable
- application should run locally with minimal setup
- build should be repeatable through Maven
- deployment should be a basic Tomcat or container-based setup
- the solution should be easy to maintain for small team use

## Use-case diagram (text description)
Actors: Warehouse User, Operations Manager, Inventory Controller

Use cases:
- Add material
- Update material
- Record stock-in
- Record stock-out
- View stock status
- Search material catalogue
- View low-stock alert

## Architecture diagram (text)
Client Browser -> Spring MVC Controller -> Service Layer -> Repository Layer -> MySQL Database
                                                        |
                                                     Validation and status logic

## Data model
### Material
- id
- materialCode
- name
- category
- unit
- quantity
- minimumQuantity
- status
- createdAt
- updatedAt

### Transaction
- id
- materialId
- transactionType
- quantity
- transactionDate
- remarks

## API list
- GET /api/materials
- GET /api/materials/{id}
- POST /api/materials
- PUT /api/materials/{id}
- GET /api/transactions
- POST /api/transactions

## Selected technology stack
- Java 21
- Spring Boot 4.0.0
- Maven Wrapper
- MySQL 8
- Thymeleaf + JavaScript UI
- Embedded Tomcat

## Local development setup
1. Install JDK 21
2. Install Maven or use the wrapper
3. Start MySQL
4. Create database `raw_material_inventory`
5. Configure `application.properties`
6. Run `./mvnw spring-boot:run`
7. Visit `http://localhost:8080`
