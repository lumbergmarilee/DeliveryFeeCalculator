# DeliveryFeeCalculator

A REST API that calculates food delivery fees based on city, vehicle type, and real-time weather conditions in Estonia. Built with Java, Spring Boot, and H2 database.

## Technologies

- **Java 17+** — the core programming language
- **Spring Boot 4.x** — framework for building the REST API, dependency injection, and scheduling
- **Spring Data JPA** — simplifies database access with automatic CRUD operations
- **H2 Database** — lightweight in-memory database for storing weather observations
- **JUnit 5** — unit testing framework
- **Estonian Weather Service XML API** — external data source for real-time weather observations

## How It Works

The application calculates delivery fees using a combination of **regional base fees** and **weather-based extra fees**.

**Base fees** depend on the city and vehicle type. For example, a bike delivery in Tartu has a base fee of 2.50€, while a car delivery in Tallinn costs 4.00€.

**Extra fees** are added based on current weather conditions:

- **Air temperature fee** (Scooter & Bike only): extra charge when temperature drops below 0°C
- **Wind speed fee** (Bike only): extra charge when wind is between 10–20 m/s. Above 20 m/s, bike delivery is forbidden
- **Weather phenomenon fee** (Scooter & Bike only): extra charge for snow, sleet, or rain. Delivery is forbidden during glaze, hail, or thunder

Weather data is automatically imported from the Estonian Weather Service every hour and on application startup.

## Project Structure

```
src/main/java/com/lumbergmarilee/delivery_fee_calculator/
│
├── DeliveryFeeCalculatorApplication.java   — App entry point, enables scheduling
│
├── controller/
│   └── DeliveryFeeController.java          — REST endpoint (GET /api/delivery-fee)
│
├── service/
│   └── DeliveryFeeService.java             — Core business logic for fee calculation
│
├── model/
│   ├── WeatherData.java                    — JPA entity for weather observations
│   └── enums/
│       ├── City.java                       — Supported cities (Tallinn, Tartu, Pärnu)
│       └── VehicleType.java                — Vehicle types (Car, Scooter, Bike)
│
├── repository/
│   └── WeatherDataRepository.java          — Database access for weather data
│
├── scheduler/
│   └── WeatherDataImporter.java            — Fetches weather data from external API
│
├── dto/
│   ├── DeliveryFeeResponse.java            — API success response format
│   └── ErrorResponse.java                  — API error response format
│
└── exception/
    ├── VehicleUsageForbiddenException.java  — Thrown when weather forbids vehicle use
    └── GlobalExceptionHandler.java         — Converts exceptions to JSON responses
```

### How the layers connect

1. A **request** comes in through the **Controller** (e.g., `GET /api/delivery-fee?cityName=TARTU&vehicleType=BIKE`)
2. The Controller calls the **Service**, which fetches the latest weather data from the **Repository**
3. The Service applies the business rules (base fee + weather extra fees) and returns the total
4. The Controller wraps the result in a JSON response and sends it back
5. Meanwhile, the **Scheduler** keeps the weather data up to date by importing from the Estonian Weather Service every hour

## How to Run

### Prerequisites

- Java 17 or higher
- Gradle (included via wrapper)

### Steps

1. After cloning the repository build and run the application by running the "DeliveryFeeCalculatorApplication"

2. Wait a few seconds for the weather data to import (you'll see "Weather data imported successfully" in the console).

3. The API is now available at `http://localhost:8080`

## API Usage

### Calculate delivery fee

```
GET /api/delivery-fee?cityName={city}&vehicleType={vehicleType}
```

**Parameters:**

| Parameter   | Values                       |
|-------------|------------------------------|
| cityName    | TALLINN, TARTU, PÄRNU        |
| vehicleType | CAR, SCOOTER, BIKE           |

**Example request:**
```
http://localhost:8080/api/delivery-fee?cityName=TARTU&vehicleType=BIKE
```

**Example response:**
```json
{
  "city": "TARTU",
  "vehicleType": "BIKE",
  "totalFee": 4.0
}
```

**Error response example:**
```json
{
  "error": "Usage of selected vehicle type is forbidden due to wind speeds"
}
```

## H2 Database Console

While the application is running, you can inspect the database at:

```
http://localhost:8080/h2-console
```

Login with:
- **JDBC URL:** `jdbc:h2:mem:devdb`
- **Username:** `sa`
- **Password:** (leave empty)
