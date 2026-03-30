package com.lumbergmarilee.delivery_fee_calculator.exception;

import com.lumbergmarilee.delivery_fee_calculator.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Global exception handler that converts exceptions into clean JSON error responses.
 * Catches exceptions thrown by controllers and services, returning appropriate HTTP status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /** Handles forbidden vehicle usage due to dangerous weather conditions. Returns 400. */
    @ExceptionHandler(VehicleUsageForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleVehicleUsageForbiddenException(VehicleUsageForbiddenException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    /** Handles invalid input parameters such as unknown city or vehicle type. Returns 400. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    /** Handles unexpected errors such as missing weather data. Returns 500. */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
