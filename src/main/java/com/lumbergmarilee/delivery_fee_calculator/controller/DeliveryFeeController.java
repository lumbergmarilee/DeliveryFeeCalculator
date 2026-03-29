package com.lumbergmarilee.delivery_fee_calculator.controller;

import com.lumbergmarilee.delivery_fee_calculator.model.enums.City;
import com.lumbergmarilee.delivery_fee_calculator.model.enums.VehicleType;
import com.lumbergmarilee.delivery_fee_calculator.service.DeliveryFeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;
    public DeliveryFeeController(DeliveryFeeService deliveryFeeService) {
        this.deliveryFeeService = deliveryFeeService;
    }

    @GetMapping("/delivery-fee")
    public double getDeliveryFee(@RequestParam String cityName, @RequestParam String vehicleType) {
        City city = City.valueOf(cityName.toUpperCase());
        VehicleType vehicle = VehicleType.valueOf(vehicleType.toUpperCase());

        double fee = deliveryFeeService.calculateFee(city, vehicle);

        return fee;
    }
}
