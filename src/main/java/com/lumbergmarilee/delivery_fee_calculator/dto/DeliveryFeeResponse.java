package com.lumbergmarilee.delivery_fee_calculator.dto;

/** API response containing the calculated delivery fee and input parameters. */
public record DeliveryFeeResponse(String city, String vehicleType, double totalFee) {}