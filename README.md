# Simple E-Commerce REST API

A stripped-down Spring Boot backend with **Product**, **Cart**, and **Order** features — written with plain Java so it's easy to read top to bottom, even if you're still learning Spring Boot.

## Tech Stack

* **Java** 17+
* **Spring Boot 3.x** (Web, Data JPA, Validation)
* **MySQL** database
* **Maven** for build & dependency management

---

## Prerequisites

Make sure you have these installed before running the project:

* JDK 17 or newer (`java -version`)
* Maven 3.8+ (`mvn -version`) — or use the included `mvnw` / `mvnw.cmd` wrapper if present
* **MySQL** 8.x installed and running locally (or accessible remotely)
* **Postman** for testing REST APIs

---

## Project Structure

```text
com.ecommerce.api
├── entity/       Product, CartItem, Order, OrderItem, OrderStatus - one table each
├── repository/   Interfaces only - Spring writes the SQL for us
├── dto/          Plain classes describing incoming request JSON, with @Valid rules
├── exception/    2 custom exceptions + 1 handler that turns them into clean JSON errors
├── service/      Business logic - the only place that calls repositories
└── controller/   REST endpoints - the only place that returns JSON to the client
```

**How a request flows through the app:**

```text
Client → Controller → Service → Repository → Database
                ↑
        DTO (validated request)
                ↓
        Response (entity / wrapper object)
```

---

## MySQL Setup

### 1. Create the database

```sql
CREATE DATABASE ecomdb;
```

### 2. Configure your connection

In `src/main/resources/application.properties`, set your own MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecomdb
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

> `ddl-auto=update` lets Hibernate create/update tables automatically based on your entities. This is convenient for development, but production applications normally manage schema changes with migrations.

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/DesaleRohit/ecommerce-backend.git
cd ecommerce-backend
```

### 2. Run the app

Using Maven directly:

```bash
mvn spring-boot:run
```

Or, if a Maven wrapper is included:

```bash
./mvnw spring-boot:run       # macOS/Linux
mvnw.cmd spring-boot:run     # Windows
```

### 3. Confirm it's running

The application starts at:

```text
http://localhost:8080
```

Open Postman and send a `GET` request to:

```text
http://localhost:8080/api/products
```

You should get an empty array `[]` on the first run if no products have been added yet.

Since MySQL is a persistent database, any data you create will remain after restarting the application.

---

## API Endpoints

### Products

| Method | Path                 | Body             | What it does      |
| ------ | -------------------- | ---------------- | ----------------- |
| POST   | `/api/products`      | `ProductRequest` | Create a product  |
| GET    | `/api/products`      | —                | List all products |
| GET    | `/api/products/{id}` | —                | Get one product   |
| PUT    | `/api/products/{id}` | `ProductRequest` | Update a product  |
| DELETE | `/api/products/{id}` | —                | Delete a product  |

### Cart

| Method | Path                           | Body                    | What it does              |
| ------ | ------------------------------ | ----------------------- | ------------------------- |
| GET    | `/api/cart`                    | —                       | View cart (items + total) |
| POST   | `/api/cart/items`              | `AddToCartRequest`      | Add a product to the cart |
| PUT    | `/api/cart/items/{cartItemId}` | `UpdateQuantityRequest` | Change quantity           |
| DELETE | `/api/cart/items/{cartItemId}` | —                       | Remove item from cart     |

### Orders

| Method | Path                      | Body                  | What it does                |
| ------ | ------------------------- | --------------------- | --------------------------- |
| POST   | `/api/orders/checkout`    | —                     | Turn the cart into an order |
| GET    | `/api/orders`             | —                     | List all orders             |
| GET    | `/api/orders/{id}`        | —                     | Get one order + its items   |
| PATCH  | `/api/orders/{id}/status` | `UpdateStatusRequest` | Change order status         |

---

## Request Body Reference

**`ProductRequest`**

```json
{
  "name": "Notebook",
  "description": "200 pages",
  "price": 50,
  "stockQuantity": 10,
  "category": "Stationery"
}
```

**`AddToCartRequest`**

```json
{
  "productId": 1,
  "quantity": 2
}
```

**`UpdateQuantityRequest`**

```json
{
  "quantity": 5
}
```

**`UpdateStatusRequest`**

```json
{
  "status": "SHIPPED"
}
```

Valid `OrderStatus` values typically include:

* `PENDING`
* `SHIPPED`
* `DELIVERED`
* `CANCELLED`

Check `OrderStatus.java` in the `entity` package for the exact enum values used in this project.

---

## Example: Full Flow

You can test the complete order flow using **Postman**:

1. Create a product using `POST /api/products`
2. Add the product to the cart using `POST /api/cart/items`
3. View the cart using `GET /api/cart`
4. Checkout using `POST /api/orders/checkout`
5. View the created order using `GET /api/orders/{id}`
6. Update the order status using `PATCH /api/orders/{id}/status`

For `POST`, `PUT`, and `PATCH` requests, select **Body → raw → JSON** in Postman and provide the appropriate request body.

---

## Error Handling

Validation errors and business-rule errors, such as adding a product that doesn't exist or ordering more than the available stock, are caught by a global exception handler and returned as clean JSON.

Example:

```json
{
  "status": 404,
  "message": "Product not found with id: 99",
  "timestamp": "2026-07-22T10:15:30"
}
```

This keeps error responses consistent across every endpoint instead of leaking raw stack traces to the client.

---

## Possible Next Steps

Once you're comfortable with this beginner version, some natural upgrades are:

* Add **Spring Security** for authentication and per-user carts
* Replace `double`/manual math with `BigDecimal` for accurate pricing
* Introduce **Lombok** to reduce boilerplate getters/setters
* Add pagination to `GET /api/products` and `GET /api/orders`
* Write unit and integration tests with **JUnit** and **MockMvc**

---

## License

This project is for learning purposes. Feel free to fork and adapt it.
