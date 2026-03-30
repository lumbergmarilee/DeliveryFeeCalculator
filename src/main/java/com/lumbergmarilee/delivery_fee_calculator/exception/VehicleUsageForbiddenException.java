package com.lumbergmarilee.delivery_fee_calculator.exception;


/**
 * Thrown when weather conditions prohibit the use of a selected vehicle type.
 * For example: wind speed over 20 m/s for bikes, or glaze/hail/thunder for bikes and scooters.
 */
public class VehicleUsageForbiddenException extends RuntimeException {
    public VehicleUsageForbiddenException(String message) {
        super(message);
    }
}
