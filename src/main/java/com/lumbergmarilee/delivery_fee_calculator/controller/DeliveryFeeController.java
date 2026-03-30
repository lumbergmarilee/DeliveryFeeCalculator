package com.lumbergmarilee.delivery_fee_calculator.controller;

import com.lumbergmarilee.delivery_fee_calculator.dto.DeliveryFeeResponse;
import com.lumbergmarilee.delivery_fee_calculator.model.enums.City;
import com.lumbergmarilee.delivery_fee_calculator.model.enums.VehicleType;
import com.lumbergmarilee.delivery_fee_calculator.service.DeliveryFeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for the delivery fee calculation API.
 * Exposes endpoint: GET /api/delivery-fee?cityName={city}&vehicleType={vehicleType}
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Delivery Fee", description = "Calculate delivery fees based on city, vehicle type, and weather")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;
    public DeliveryFeeController(DeliveryFeeService deliveryFeeService) {
        this.deliveryFeeService = deliveryFeeService;
    }


    /**
     * Calculates the delivery fee for the given city and vehicle type.
     * Uses the latest weather data from the database.
     *
     * @param cityName    the delivery city (TALLINN, TARTU, or PÄRNU)
     * @param vehicleType the vehicle type (CAR, SCOOTER, or BIKE)
     * @return DeliveryFeeResponse containing the city, vehicle type, and calculated fee
     */
    @Operation(
            summary = "Calculate delivery fee",
            description = "Calculates the total delivery fee based on city, vehicle type, and current weather conditions"
    )
    @ApiResponse(responseCode = "200", description = "Fee calculated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or vehicle usage forbidden due to weather")
    @ApiResponse(responseCode = "500", description = "Weather data not available")
    @GetMapping("/delivery-fee")
    public DeliveryFeeResponse getDeliveryFee(
            @Parameter(description = "City name (TALLINN, TARTU, or PÄRNU)") @RequestParam String cityName,
            @Parameter(description = "Vehicle type (CAR, SCOOTER, or BIKE)") @RequestParam String vehicleType) {
        City city = City.valueOf(cityName.toUpperCase());
        VehicleType vehicle = VehicleType.valueOf(vehicleType.toUpperCase());

        double fee = deliveryFeeService.calculateFee(city, vehicle);

        return new DeliveryFeeResponse(cityName.toUpperCase(), vehicleType.toUpperCase(), fee);
    }
}
